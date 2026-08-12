package com.example.billingservice.application.service;

import com.example.billingservice.application.Utils.InvoiceCreditNoteStatusPassagePolicy;
import com.example.billingservice.application.Utils.StatusMapper;
import com.example.billingservice.application.ports.in.GenerateInvoiceNumberUseCase;
import com.example.billingservice.application.ports.in.InvoiceCreditNoteUseCase;
import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.application.ports.out.AuditEventPublisherPort;
import com.example.billingservice.application.ports.out.ClientInvoicesRepositoryPort;
import com.example.billingservice.application.ports.out.InvoiceCreditNoteRepositoryPort;
import com.example.billingservice.application.ports.out.SupplierInvoicesRepositoryPort;
import com.example.billingservice.domain.enums.*;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.*;
import com.example.billingservice.infrastructure.out.messaging.AuditEvent;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.infrastructure.out.persistance.mapper.InvoiceCreditNoteMapper;
import com.example.billingservice.shared.CreditNoteAuditEventFactory;
import com.example.billingservice.shared.ParseEnum;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class InvoiceCreditNoteService implements InvoiceCreditNoteUseCase {

    private final InvoiceCreditNoteRepositoryPort invoiceCreditNoteRepositoryPort;
    private final SupplierInvoicesRepositoryPort supplierInvoicesRepositoryPort;
    private final ClientInvoicesRepositoryPort clientInvoicesRepositoryPort;
    private final UploadDocumentService uploadDocumentService;
    private final InvoiceCreditNoteMapper invoiceCreditNoteMapper;
    private final GenerateInvoiceNumberUseCase generateInvoiceNumberUseCase;
    private final AuditEventPublisherPort auditEventPublisherPort;
    private final CreditNoteAuditEventFactory creditNoteAuditEventFactory;
    private final PartnerUseCase partnerUseCase;


    @Override
    public Page<InvoiceCreditNotePageItemDTO> getInvoiceCreditNotes(UUID idInvoice, String keyword, String status, int page) {

        InvoiceCreditNoteStatus invoiceCreditNoteStatus = ParseEnum.parseEnum(status, InvoiceCreditNoteStatus.class);

        if(!supplierInvoicesRepositoryPort.existsByInvoiceId(idInvoice)
            && !clientInvoicesRepositoryPort.existsByInvoiceId(idInvoice)
        ){
            throw BillingException.notFound("Facture", String.valueOf(idInvoice));
        }
        return invoiceCreditNoteRepositoryPort.getInvoiceCreditNotes(idInvoice,keyword,invoiceCreditNoteStatus,page);
    }

    @Override
    public InvoiceCreditNote getInvoiceCreditNote(UUID idInvoiceCreditNote) {
        if (!invoiceCreditNoteRepositoryPort.existsByInvoiceCreditNoteId(idInvoiceCreditNote)) {
            throw BillingException.notFound("Facture d'avoir", String.valueOf(idInvoiceCreditNote));
        }
        return invoiceCreditNoteRepositoryPort.getById(idInvoiceCreditNote);
    }

    @Override
    public InvoiceCreditNoteDetailsDTO getInvoiceCreditNoteByInvoiceCreditNoteNumber(String creditNoteNumber) {
        if (!invoiceCreditNoteRepositoryPort.existsByInvoiceCreditNoteNumber(creditNoteNumber)) {
            throw BillingException.notFound("Facture d'avoir", String.valueOf(creditNoteNumber));
        }
        InvoiceCreditNote invoiceCreditNote = invoiceCreditNoteRepositoryPort.getByInvoiceCreditNoteNumber(creditNoteNumber);
        return invoiceCreditNoteMapper.toDetailsDTO(invoiceCreditNote);
    }

    @Override
    public InvoiceCreditNoteDTO create(InvoiceCreditNoteCreateDTO createDTO) throws IOException {
        Invoice invoice ;

        if(clientInvoicesRepositoryPort.existsByInvoiceId(UUID.fromString(createDTO.getOriginalInvoiceId()))){
            invoice = clientInvoicesRepositoryPort.getInvoice(UUID.fromString(createDTO.getOriginalInvoiceId()));
        } else if (supplierInvoicesRepositoryPort.existsByInvoiceId(UUID.fromString(createDTO.getOriginalInvoiceId()))) {
            invoice = supplierInvoicesRepositoryPort.getInvoice(UUID.fromString(createDTO.getOriginalInvoiceId()));
        }
        else {
            throw BillingException.notFound("Facture",createDTO.getOriginalInvoiceId());
        }



        if (createDTO.getInvoiceCreditNoteNumber() != null && existsByInvoiceCreditNoteNumber(createDTO.getInvoiceCreditNoteNumber())) {
            throw BillingException.alreadyExists("Facture d'avoir", "invoiceCreditNoteNumber", createDTO.getInvoiceCreditNoteNumber());
        }

        if(createDTO.getIssueDate().before(invoice.getIssueDate())){
            throw BillingException.badRequest("La date d'émission de l'avoir ne peut pas être antérieure à celle de la facture associée");
        }

        if(invoice.getInvoiceStatus()== InvoiceStatus.DRAFT
                || invoice.getInvoiceStatus()== InvoiceStatus.CANCELLED)
        {

            String status = StatusMapper.mapInvoiceStatusToFrench(invoice.getInvoiceStatus());

            throw BillingException
                    .badRequest("Impossible de créer une facture d'avoir à partir d'une facture "
                            +status);
        }

        String invoiceNumber = generateInvoiceNumberUseCase.generate(SequenceNumberType.CREDIT_NOTE);
        createDTO.setInvoiceCreditNoteNumber(invoiceNumber);

        Document invoiceDocument = null;
        if (createDTO.getInvoiceDocument() != null && !createDTO.getInvoiceDocument().isEmpty()) {

            UploadedFile document = new UploadedFile(
                    createDTO.getInvoiceDocument().getOriginalFilename(),
                    createDTO.getInvoiceDocument().getContentType(),
                    createDTO.getInvoiceDocument().getInputStream(),
                    createDTO.getInvoiceDocument().getSize()
            );

            invoiceDocument = uploadDocumentService.upload(
                    createDTO.getInvoiceCreditNoteNumber(),
                    DocumentType.INVOICE,
                    document
            );
        }

        InvoiceCreditNote invoiceCreditNote = invoiceCreditNoteMapper.toDomain(createDTO, invoiceDocument, invoice);

        System.out.println("InvoiceCreditNote: "+invoiceCreditNote);
/*
        SyncInvoiceItems.syncInvoiceItems(
                invoice,
                createDTO.getInvoiceItems() != null ? createDTO.getInvoiceItems() : List.of()
        );
*/
        //System.out.println("createdInvoice:"+invoiceCreditNote);

        InvoiceCreditNote savedInvoiceCreditNote = invoiceCreditNoteRepositoryPort.create(invoiceCreditNote);
        System.out.println("savedInvoiceCreditNote: "+savedInvoiceCreditNote);


        generateInvoiceNumberUseCase.validateNextSequence(SequenceNumberType.CREDIT_NOTE, invoiceNumber);


        InvoiceCreditNoteDTO invoiceCreditNoteDTO=  invoiceCreditNoteMapper.toDTO(savedInvoiceCreditNote);

        AuditEvent auditEvent = creditNoteAuditEventFactory.creditNoteCreated(
                invoiceCreditNoteDTO.getIdInvoiceCreditNote(),
                String.valueOf(invoiceCreditNoteDTO.getIdInvoiceCreditNote()),
                Map.of("credit-note-number", invoiceCreditNoteDTO.getInvoiceCreditNoteNumber()),
                null
        );

        auditEventPublisherPort.publish(auditEvent);

        return invoiceCreditNoteDTO;
    }

    @Override
    public InvoiceCreditNoteDetailsDTO updateInvoiceCreditNoteStatus(String creditNoteNumber, InvoiceCreditNoteStatus invoiceCreditNoteStatus) {

        if(!invoiceCreditNoteRepositoryPort.existsByInvoiceCreditNoteNumber(creditNoteNumber)){
            throw   BillingException.notFound("Facture d'avoir", String.valueOf(creditNoteNumber));
        }

        InvoiceCreditNote invoiceCreditNote = invoiceCreditNoteRepositoryPort.getByInvoiceCreditNoteNumber(creditNoteNumber);

        InvoiceCreditNoteStatusPassagePolicy.checkTransition(
                invoiceCreditNote.getInvoiceCreditNoteStatus(), invoiceCreditNoteStatus);

        List<InvoiceCreditNoteEvent> invoiceEvents = invoiceCreditNote.getInvoiceCreditNoteEvents() != null
                ? invoiceCreditNote.getInvoiceCreditNoteEvents()
                : List.of();

        InvoiceCreditNoteEvent invoiceEvent = InvoiceCreditNoteEvent.builder()
                .invoiceCreditNoteEventType(InvoiceCreditNoteEventType.UPDATED)
                .eventDate(new Date())
                .description("Mise à jour de satut facture : "+ AuditEventTrigger.USER.name())
                .eventTrigger(AuditEventTrigger.USER)
                .triggeredBy("user: wassef")
                .build();


        List<InvoiceCreditNoteEvent> updatedEvents = new ArrayList<>(invoiceEvents);

        updatedEvents.add(invoiceEvent);

        invoiceCreditNote.setInvoiceCreditNoteEvents(updatedEvents);

        InvoiceCreditNote updatedInvoiceCreditNote = invoiceCreditNoteRepositoryPort.updateStatus(creditNoteNumber, invoiceCreditNoteStatus);



        InvoiceCreditNoteDetailsDTO invoiceCreditNoteDetailsDTO =  invoiceCreditNoteMapper.toDetailsDTO(updatedInvoiceCreditNote);

        AuditEvent auditEvent = creditNoteAuditEventFactory.creditNoteStatusChanged(
                invoiceCreditNoteDetailsDTO.getIdInvoiceCreditNote(),
                String.valueOf(invoiceCreditNoteDetailsDTO.getIdInvoiceCreditNote()),
                invoiceCreditNote.getInvoiceCreditNoteStatus().name(),
                invoiceCreditNoteStatus.name(),
                null
        );

        auditEventPublisherPort.publish(auditEvent);

        return invoiceCreditNoteDetailsDTO;
    }

    @Override
    public void deleteInvoiceCreditNote(UUID invoiceCreditNoteId) {
        if(!invoiceCreditNoteRepositoryPort.existsByInvoiceCreditNoteId(invoiceCreditNoteId)){
            throw BillingException.notFound("Facture d'avoir", String.valueOf(invoiceCreditNoteId));
        }

        InvoiceCreditNote invoiceCreditNote = invoiceCreditNoteRepositoryPort.getById(invoiceCreditNoteId);

        if(invoiceCreditNote!=null &&
                invoiceCreditNote.getInvoiceCreditNoteStatus()!=InvoiceCreditNoteStatus.DRAFT)
        {
            updateInvoiceCreditNoteStatus(
                    invoiceCreditNote.getInvoiceCreditNoteNumber(),
                    InvoiceCreditNoteStatus.ARCHIVED
            );

            AuditEvent auditEvent = creditNoteAuditEventFactory.creditNoteStatusChanged(
                    invoiceCreditNote.getIdInvoiceCreditNote(),
                    String.valueOf(invoiceCreditNote.getIdInvoiceCreditNote()),
                    invoiceCreditNote.getInvoiceCreditNoteStatus().name(),
                    InvoiceCreditNoteStatus.ARCHIVED.name(),
                    null
            );

            auditEventPublisherPort.publish(auditEvent);
            return;
        }

        invoiceCreditNoteRepositoryPort.delete(invoiceCreditNoteId);



    }


    @Override
    public boolean existsByInvoiceCreditNoteNumber(String invoiceCreditNoteNumber) {
        return invoiceCreditNoteRepositoryPort.existsByInvoiceCreditNoteNumber(invoiceCreditNoteNumber);
    }

    @Override
    public boolean existsByInvoiceCreditNoteId(UUID invoiceCreditNoteId) {
        return invoiceCreditNoteRepositoryPort.existsByInvoiceCreditNoteId(invoiceCreditNoteId);
    }

    @Override
    public boolean existsInvoiceCreditNoteEntityByInvoice(UUID idInvoice) {
        return invoiceCreditNoteRepositoryPort.existsInvoiceCreditNoteEntityByInvoice(idInvoice);
    }

    @Override
    public boolean hasCreditNotesWithStatus(UUID invoiceId, InvoiceCreditNoteStatus invoiceCreditNoteStatus) {
        return invoiceCreditNoteRepositoryPort.hasCreditNotesWithStatus(invoiceId, invoiceCreditNoteStatus);
    }

    @Override
    public Page<InvoiceCreditNotePageItemDTO> getCreditNoteInvoiceByPartner(String idPartner , String partnerType, int page) {
        if(partnerType.equals(PartnerType.CLIENT.toString()) &&
            !partnerUseCase.customerExistsByIdPartner(UUID.fromString(idPartner)))
        {
            throw BillingException.notFound("Client", idPartner);
        }
        if (partnerType.equals(PartnerType.SUPPLIER.toString()) &&
                !partnerUseCase.supplierExistsByIdPartner(UUID.fromString(idPartner)))
        {
            throw BillingException.notFound("Fournisseur", idPartner);
        }
        return invoiceCreditNoteRepositoryPort.getCreditNoteByClient(UUID.fromString(idPartner), page);
    }




}
