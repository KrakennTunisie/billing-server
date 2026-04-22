package com.example.billingservice.application.service;

import com.example.billingservice.application.Utils.InvoiceStatusPassagePolicy;
import com.example.billingservice.application.ports.in.GenerateInvoiceNumberUseCase;
import com.example.billingservice.application.ports.in.InvoiceCreditNoteUseCase;
import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.application.ports.out.ClientInvoicesRepositoryPort;
import com.example.billingservice.application.ports.out.SupplierInvoicesRepositoryPort;
import com.example.billingservice.domain.enums.*;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.*;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.infrastructure.out.persistance.mapper.InvoiceMapper;
import com.example.billingservice.shared.ParseEnum;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;


@Service
@AllArgsConstructor
public class InvoiceService implements InvoiceUseCase {

    private final InvoiceMapper invoiceMapper;
    private final UploadDocumentService uploadDocumentService;
    private final GenerateInvoiceNumberUseCase generateInvoiceNumberUseCase;
    private final PartnerUseCase partnerUseCase;
    private final ClientInvoicesRepositoryPort clientInvoicesRepositoryPort;
    private final SupplierInvoicesRepositoryPort supplierInvoicesRepositoryPort;
    private final InvoiceCreditNoteUseCase invoiceCreditNoteUseCase;

    @Override
    public InvoiceDTO createInvoice(InvoiceCreateDTO createDTO) throws IOException, BillingException {
        if (!createDTO.getInvoiceType().equals(InvoiceType.PURCHASE.name())) {
            throw BillingException.badRequest("il faut avoir un facture d'achat.");
        }
        if (!partnerUseCase.supplierExistsByIdPartner(UUID.fromString(createDTO.getPartner()))) {
            throw BillingException.notFound("Fournisseur", createDTO.getPartner());
        }
        return createBaseInvoice(createDTO);
    }

    @Override
    public InvoiceDTO createClientInvoice(InvoiceCreateDTO createDTO) throws IOException {
        if (!createDTO.getInvoiceType().equals(InvoiceType.SALE.name())) {
            throw BillingException.badRequest("il faut avoir un facture de vente.");
        }

        if (!partnerUseCase.customerExistsByIdPartner(UUID.fromString(createDTO.getPartner()))) {
            throw BillingException.notFound("Client", createDTO.getPartner());
        }

        return createBaseInvoice(createDTO);

    }

    @Override
    public InvoiceDTO updateInvoice(InvoiceUpdateDTO invoiceUpdateDTO) throws IOException {
        if (!invoiceUpdateDTO.getInvoiceType().equals(InvoiceType.PURCHASE.name())) {
            throw BillingException.badRequest("il faut avoir un facture d'achat.");
        }
        if(invoiceUpdateDTO.getIdInvoice() != null
                && !supplierInvoicesRepositoryPort.existsByInvoiceId(invoiceUpdateDTO.getIdInvoice())){
            throw BillingException.notFound("Facture", String.valueOf(invoiceUpdateDTO.getIdInvoice()));
        }

        InvoiceDTO invoiceDTO = getInvoiceById(invoiceUpdateDTO.getIdInvoice());
        Invoice invoice =  this.updateBaseInvoice(invoiceUpdateDTO, invoiceDTO);

        return supplierInvoicesRepositoryPort.update(invoice);
    }

    @Override
    public InvoiceDTO updateClientInvoice(InvoiceUpdateDTO invoiceUpdateDTO) throws IOException {
        if (!invoiceUpdateDTO.getInvoiceType().equals(InvoiceType.SALE.name())) {
            throw BillingException.badRequest("il faut avoir un facture de vente.");
        }
        if(invoiceUpdateDTO.getIdInvoice() != null
                && !clientInvoicesRepositoryPort.existsByInvoiceId(invoiceUpdateDTO.getIdInvoice())){
            throw BillingException.notFound("Facture", String.valueOf(invoiceUpdateDTO.getIdInvoice()));
        }

        InvoiceDTO invoiceDTO = getClientInvoiceById(invoiceUpdateDTO.getIdInvoice());
        Invoice invoice =  this.updateBaseInvoice(invoiceUpdateDTO, invoiceDTO);

        return clientInvoicesRepositoryPort.update(invoice);
    }

    @Override
    public InvoiceDTO updateInvoiceStatus(UUID invoiceId, InvoiceStatus invoiceStatus) {

        if(!supplierInvoicesRepositoryPort.existsByInvoiceId(invoiceId)){
          throw BillingException.notFound("Facture Fournisseur", String.valueOf(invoiceId));
        }

        InvoiceDTO invoiceDTO = supplierInvoicesRepositoryPort.getById(invoiceId);

        InvoiceStatusPassagePolicy.checkTransition(invoiceDTO.getInvoiceStatus(), invoiceStatus);

        List<InvoiceEvent> invoiceEvents = invoiceDTO.getInvoiceEvents() != null
                ? invoiceDTO.getInvoiceEvents()
                : List.of();

        InvoiceEvent invoiceEvent = InvoiceEvent.builder()
                .invoiceEventType(InvoiceEventType.UPDATED)
                .eventDate(new Date())
                .description("Mise à jour de satut facture : "+InvoiceEventTrigger.USER.name())
                .eventTrigger(InvoiceEventTrigger.USER)
                .triggeredBy("user: wassef")
                .build();


        List<InvoiceEvent> updatedEvents = new ArrayList<>(invoiceEvents);

        updatedEvents.add(invoiceEvent);

        invoiceDTO.setInvoiceEvents(updatedEvents);

        return supplierInvoicesRepositoryPort.updateStatus(invoiceId, invoiceStatus);
    }

    @Override
    public InvoiceDTO updateClientInvoiceStatus(UUID invoiceId, InvoiceStatus invoiceStatus) {

        if(!clientInvoicesRepositoryPort.existsByInvoiceId(invoiceId)){
            throw BillingException.notFound("Facture Client", String.valueOf(invoiceId));
        }

        InvoiceDTO invoiceDTO = clientInvoicesRepositoryPort.getById(invoiceId);

        InvoiceStatusPassagePolicy.checkTransition(invoiceDTO.getInvoiceStatus(), invoiceStatus);

        List<InvoiceEvent> invoiceEvents = invoiceDTO.getInvoiceEvents() != null
                ? invoiceDTO.getInvoiceEvents()
                : List.of();

        InvoiceEvent invoiceEvent = InvoiceEvent.builder()
                .invoiceEventType(InvoiceEventType.UPDATED)
                .eventDate(new Date())
                .description("Mise à jour de satut facture : "+InvoiceEventTrigger.USER.name())
                .eventTrigger(InvoiceEventTrigger.USER)
                .triggeredBy("user: wassef")
                .build();


        List<InvoiceEvent> updatedEvents = new ArrayList<>(invoiceEvents);

        updatedEvents.add(invoiceEvent);

        invoiceDTO.setInvoiceEvents(updatedEvents);

        return clientInvoicesRepositoryPort.updateStatus(invoiceId, invoiceStatus);
    }

    @Override
    public InvoiceDTO getInvoiceById(UUID invoiceId) {
        if(!supplierInvoicesRepositoryPort.existsByInvoiceId(invoiceId)){
            throw  BillingException.notFound("Facture Fournisseur", String.valueOf(invoiceId));
        }
        InvoiceDTO invoiceDTO = supplierInvoicesRepositoryPort.getById(invoiceId);
        if(invoiceCreditNoteUseCase.existsInvoiceCreditNoteEntityByInvoice(invoiceId)){
            invoiceDTO.setHasInvoiceCreditNotes(true);
        }
        return invoiceDTO;
    }

    @Override
    public InvoiceDTO getClientInvoiceById(UUID invoiceId) {
        if(!clientInvoicesRepositoryPort.existsByInvoiceId(invoiceId)){
            throw  BillingException.notFound("Facture Client", String.valueOf(invoiceId));
        }
        InvoiceDTO invoiceDTO = clientInvoicesRepositoryPort.getById(invoiceId);
        if(invoiceCreditNoteUseCase.existsInvoiceCreditNoteEntityByInvoice(invoiceId)){
            invoiceDTO.setHasInvoiceCreditNotes(true);
        }
        return invoiceDTO;
    }

    @Override
    public Invoice getInvoiceDomainById(UUID invoiceId) {
        if(!supplierInvoicesRepositoryPort.existsByInvoiceId(invoiceId)){
            throw  BillingException.notFound("Facture Fourrnisseur", String.valueOf(invoiceId));
        }
        return supplierInvoicesRepositoryPort.getInvoice(invoiceId);
    }

    @Override
    public Invoice getClientInvoiceDomainById(UUID invoiceId) {
        if(!clientInvoicesRepositoryPort.existsByInvoiceId(invoiceId)){
            throw  BillingException.notFound("Facture Client", String.valueOf(invoiceId));
        }
        return supplierInvoicesRepositoryPort.getInvoice(invoiceId);    }

    @Override
    @Transactional
    public void deleteInvoice(UUID invoiceId) {
        if(!supplierInvoicesRepositoryPort.existsByInvoiceId(invoiceId)){
            throw BillingException.notFound("Facture Fournisseur", String.valueOf(invoiceId));
        }
        supplierInvoicesRepositoryPort.delete(invoiceId);
    }

    @Override
    public void deleteClientInvoice(UUID invoiceId) {
        if(!clientInvoicesRepositoryPort.existsByInvoiceId(invoiceId)){
            throw BillingException.notFound("Facture Fournisseur", String.valueOf(invoiceId));
        }
        clientInvoicesRepositoryPort.delete(invoiceId);
    }

    @Override
    public boolean existsByInvoiceNumber(String invoiceNumber) {
        return supplierInvoicesRepositoryPort.existsByInvoiceNumber(invoiceNumber);
    }

    @Override
    public boolean clientInvoiceExistsByInvoiceNumber(String invoiceNumber) {
        return clientInvoicesRepositoryPort.existsByInvoiceNumber(invoiceNumber);
    }

    @Override
    public boolean existsByInvoiceId(UUID invoiceId) {
        return supplierInvoicesRepositoryPort.existsByInvoiceId(invoiceId);
    }

    @Override
    public boolean clientInvoiceExistsByInvoiceId(UUID invoiceId) {
        return clientInvoicesRepositoryPort.existsByInvoiceId(invoiceId);
    }

    @Override
    public Page<InvoicePageItemDTO> getClientsInvoices(String keyword, String status, int page) {
        InvoiceStatus invoiceStatus = ParseEnum.parseEnum(status, InvoiceStatus.class);

        return clientInvoicesRepositoryPort.findAllInvoices(keyword, invoiceStatus, page, InvoiceType.SALE);
    }

    @Override
    public Page<InvoicePageItemDTO> getSuppliersInvoices(String keyword, String status, int page) {

        InvoiceStatus invoiceStatus = ParseEnum.parseEnum(status, InvoiceStatus.class);

        return supplierInvoicesRepositoryPort.findAllInvoices(keyword, invoiceStatus, page, InvoiceType.SALE);
    }


    private void mapItemFields(InvoiceItem source, InvoiceItem target) {
        target.setDescription(source.getDescription());
        target.setQuantity(source.getQuantity());
        target.setUnityPriceEXclTax(source.getUnityPriceEXclTax());
        target.setVatRate(source.getVatRate());
    }

    private InvoiceDTO createBaseInvoice(InvoiceCreateDTO createDTO) throws IOException {


        String invoiceNumber = generateInvoiceNumberUseCase.generate(SequenceNumberType.INVOICE);

        Document invoiceDocument = null;
        if (createDTO.getInvoiceDocument() != null && !createDTO.getInvoiceDocument().isEmpty()) {
            UploadedFile document = new UploadedFile(
                    createDTO.getInvoiceDocument().getOriginalFilename(),
                    createDTO.getInvoiceDocument().getContentType(),
                    createDTO.getInvoiceDocument().getBytes()
            );

            invoiceDocument = uploadDocumentService.upload(
                    invoiceNumber,
                    DocumentType.INVOICE,
                    document
            );
        }

        Invoice invoice = invoiceMapper.invoiceCreateDTOtoDomain(createDTO, invoiceDocument, invoiceNumber);

/*
        SyncInvoiceItems.syncInvoiceItems(
                invoice,
                createDTO.getInvoiceItems() != null ? createDTO.getInvoiceItems() : List.of()
        );
*/
        //System.out.println("createdInvoice:"+invoice.getInvoiceType());

        InvoiceDTO savedInvoice =
                invoice.getInvoiceType() == InvoiceType.PURCHASE
                ?
                supplierInvoicesRepositoryPort.save(invoice)
                :
                clientInvoicesRepositoryPort.save(invoice);

        generateInvoiceNumberUseCase.validateNextSequence(SequenceNumberType.INVOICE, invoiceNumber);


        return savedInvoice;
    }

    private Invoice updateBaseInvoice(InvoiceUpdateDTO invoiceUpdateDTO, InvoiceDTO invoiceDTO) throws IOException {


        if(invoiceUpdateDTO.getInvoiceNumber() != null
                && !invoiceUpdateDTO.getInvoiceNumber().equals(invoiceDTO.getInvoiceNumber())){
            throw BillingException.badRequest("Impossible de mettre à jour le numéro de document");
        }
        InvoiceStatusPassagePolicy
                .checkTransition(
                        invoiceDTO.getInvoiceStatus(),
                        InvoiceStatus.valueOf(invoiceUpdateDTO.getInvoiceStatus())
                );

        UploadedFile document = new UploadedFile(
                invoiceUpdateDTO.getInvoiceDocument().getOriginalFilename(),
                invoiceUpdateDTO.getInvoiceDocument().getContentType(),
                invoiceUpdateDTO.getInvoiceDocument().getBytes()
        );


        Document invoiceDocument =
                uploadDocumentService.upload(invoiceUpdateDTO.getInvoiceNumber(), DocumentType.INVOICE, document);

        invoiceUpdateDTO.setPartner(String.valueOf(invoiceDTO.getPartner().getIdPartner()));


        Invoice invoice = invoiceMapper.updateDTOtoDomain(invoiceUpdateDTO, invoiceDTO, invoiceDocument);


/*        SyncInvoiceItems.syncInvoiceItems(
                invoice,
                invoiceUpdateDTO.getInvoiceItems() != null ? invoiceUpdateDTO.getInvoiceItems() : List.of()
        );*/

        return invoice;
    }


}
