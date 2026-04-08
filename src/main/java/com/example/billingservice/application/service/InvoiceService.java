package com.example.billingservice.application.service;

import com.example.billingservice.application.Utils.InvoiceStatusPassagePolicy;
import com.example.billingservice.application.Utils.SyncInvoiceItems;
import com.example.billingservice.application.ports.in.GenerateInvoiceNumberUseCase;
import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.application.ports.out.InvoiceRepositoryPort;
import com.example.billingservice.domain.enums.*;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.infrastructure.out.persistance.mapper.InvoiceMapper;
import com.example.billingservice.shared.ParseEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class InvoiceService implements InvoiceUseCase {

    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final InvoiceMapper invoiceMapper;
    private final UploadDocumentService uploadDocumentService;
    private final GenerateInvoiceNumberUseCase generateInvoiceNumberUseCase;
    private final PartnerUseCase partnerUseCase;

    @Override
    public InvoiceDTO createInvoice(InvoiceCreateDTO createDTO) throws IOException, BillingException {

        if (!partnerUseCase.customerExistsByIdPartner(UUID.fromString(createDTO.getPartner()))
                && !partnerUseCase.supplierExistsByIdPartner(UUID.fromString(createDTO.getPartner()))) {
            throw BillingException.notFound("Partner", createDTO.getPartner());
        }

        if (createDTO.getInvoiceNumber() != null && existsByInvoiceNumber(createDTO.getInvoiceNumber())) {
            throw BillingException.alreadyExists("Facture", "invoiceNumber", createDTO.getInvoiceNumber());
        }

        String invoiceNumber = generateInvoiceNumberUseCase.generate(SequenceNumberType.INVOICE);
        createDTO.setInvoiceNumber(invoiceNumber);

        Document invoiceDocument = null;
        if (createDTO.getInvoiceDocument() != null && !createDTO.getInvoiceDocument().isEmpty()) {
            UploadedFile document = new UploadedFile(
                    createDTO.getInvoiceDocument().getOriginalFilename(),
                    createDTO.getInvoiceDocument().getContentType(),
                    createDTO.getInvoiceDocument().getBytes()
            );

            invoiceDocument = uploadDocumentService.upload(
                    createDTO.getInvoiceNumber(),
                    DocumentType.INVOICE,
                    document
            );
        }

        Invoice invoice = invoiceMapper.invoiceCreateDTOtoDomain(createDTO, invoiceDocument);

/*
        SyncInvoiceItems.syncInvoiceItems(
                invoice,
                createDTO.getInvoiceItems() != null ? createDTO.getInvoiceItems() : List.of()
        );
*/
        System.out.println("createdInvoice:"+invoice);

        InvoiceDTO savedInvoice = invoiceRepositoryPort.save(invoice);

        generateInvoiceNumberUseCase.validateNextSequence(SequenceNumberType.INVOICE, invoiceNumber);


        return savedInvoice;
    }

    @Override
    public InvoiceDTO updateInvoice(InvoiceUpdateDTO invoiceUpdateDTO) throws IOException {

        if(invoiceUpdateDTO.getIdInvoice() != null && !existsByInvoiceId(invoiceUpdateDTO.getIdInvoice())){
            throw BillingException.notFound("Facture", String.valueOf(invoiceUpdateDTO.getIdInvoice()));
        }

        InvoiceDTO invoiceDTO = getInvoiceById(invoiceUpdateDTO.getIdInvoice());

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


        return invoiceRepositoryPort.update(invoice);
    }

    @Override
    public InvoiceDTO updateInvoiceStatus(UUID invoiceId, InvoiceStatus invoiceStatus) {

        if(!invoiceRepositoryPort.existsByInvoiceId(invoiceId)){
          throw BillingException.notFound("Facture", String.valueOf(invoiceId));
        }

        InvoiceDTO invoiceDTO = getInvoiceById(invoiceId);

        InvoiceStatusPassagePolicy.checkTransition(invoiceDTO.getInvoiceStatus(), invoiceStatus);

        return invoiceRepositoryPort.updateStatus(invoiceId, invoiceStatus);
    }

    @Override
    public InvoiceDTO getInvoiceById(UUID invoiceId) {
        if(!existsByInvoiceId(invoiceId)){
            throw  BillingException.notFound("Facture", String.valueOf(invoiceId));
        }
        return invoiceRepositoryPort.getById(invoiceId);
    }

    @Override
    public Invoice getInvoiceDomainById(UUID invoiceId) {
        if(!existsByInvoiceId(invoiceId)){
            throw  BillingException.notFound("Facture", String.valueOf(invoiceId));
        }
        return invoiceRepositoryPort.getInvoice(invoiceId);
    }

    @Override
    @Transactional
    public void deleteInvoice(UUID invoiceId) {
        if(!existsByInvoiceId(invoiceId)){
            throw BillingException.notFound("Facture", String.valueOf(invoiceId));
        }
        invoiceRepositoryPort.delete(invoiceId);
    }

    @Override
    public boolean existsByInvoiceNumber(String invoiceNumber) {
        return invoiceRepositoryPort.existsByInvoiceNumber(invoiceNumber);
    }

    @Override
    public boolean existsByInvoiceId(UUID invoiceId) {
        return invoiceRepositoryPort.existsByInvoiceId(invoiceId);
    }

    @Override
    public Page<InvoicePageItemDTO> getClientsInvoices(String keyword, String status, int page) {
        InvoiceStatus invoiceStatus = ParseEnum.parseEnum(status, InvoiceStatus.class);

        return invoiceRepositoryPort.findAllInvoices(keyword, invoiceStatus, page, InvoiceType.SALE);
    }


    private void mapItemFields(InvoiceItem source, InvoiceItem target) {
        target.setDescription(source.getDescription());
        target.setQuantity(source.getQuantity());
        target.setUnityPriceEXclTax(source.getUnityPriceEXclTax());
        target.setVatRate(source.getVatRate());
    }


}
