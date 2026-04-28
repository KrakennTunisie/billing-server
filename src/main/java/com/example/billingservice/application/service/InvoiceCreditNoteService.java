package com.example.billingservice.application.service;

import com.example.billingservice.application.Utils.InvoiceCreditNoteStatusPassagePolicy;
import com.example.billingservice.application.Utils.StatusMapper;
import com.example.billingservice.application.ports.in.GenerateInvoiceNumberUseCase;
import com.example.billingservice.application.ports.in.InvoiceCreditNoteUseCase;
import com.example.billingservice.application.ports.out.ClientInvoicesRepositoryPort;
import com.example.billingservice.application.ports.out.InvoiceCreditNoteRepositoryPort;
import com.example.billingservice.application.ports.out.SupplierInvoicesRepositoryPort;
import com.example.billingservice.domain.enums.*;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.*;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.infrastructure.out.persistance.mapper.InvoiceCreditNoteMapper;
import com.example.billingservice.shared.ParseEnum;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InvoiceCreditNoteService implements InvoiceCreditNoteUseCase {

    private final InvoiceCreditNoteRepositoryPort invoiceCreditNoteRepositoryPort;
    private final SupplierInvoicesRepositoryPort supplierInvoicesRepositoryPort;
    private final ClientInvoicesRepositoryPort clientInvoicesRepositoryPort;
    private final UploadDocumentService uploadDocumentService;
    private final InvoiceCreditNoteMapper invoiceCreditNoteMapper;
    private  final GenerateInvoiceNumberUseCase generateInvoiceNumberUseCase;


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


        return invoiceCreditNoteMapper.toDTO(savedInvoiceCreditNote);
    }

    @Override
    public InvoiceCreditNoteDetailsDTO updateInvoiceCreditNoteStatus(String creditNoteNumber, InvoiceCreditNoteStatus invoiceCreditNoteStatus) {

        if(!invoiceCreditNoteRepositoryPort.existsByInvoiceCreditNoteNumber(creditNoteNumber)){
            throw   BillingException.notFound("Facture", String.valueOf(creditNoteNumber));
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
                .description("Mise à jour de satut facture : "+InvoiceEventTrigger.USER.name())
                .eventTrigger(InvoiceEventTrigger.USER)
                .triggeredBy("user: wassef")
                .build();


        List<InvoiceCreditNoteEvent> updatedEvents = new ArrayList<>(invoiceEvents);

        updatedEvents.add(invoiceEvent);

        invoiceCreditNote.setInvoiceCreditNoteEvents(updatedEvents);

        InvoiceCreditNote updatedInvoiceCreditNote = invoiceCreditNoteRepositoryPort.updateStatus(invoiceCreditNote, invoiceCreditNoteStatus);



        return invoiceCreditNoteMapper.toDetailsDTO(updatedInvoiceCreditNote);
    }

    @Override
    public void deleteInvoiceCreditNote(UUID invoiceCreditNoteId) {
        if(!invoiceCreditNoteRepositoryPort.existsByInvoiceCreditNoteId(invoiceCreditNoteId)){
            throw BillingException.notFound("Facture d'avoir", String.valueOf(invoiceCreditNoteId));
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


}
