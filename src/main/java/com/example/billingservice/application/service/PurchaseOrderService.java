package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.GenerateInvoiceNumberUseCase;
import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.application.ports.in.PurchaseOrderUseCase;
import com.example.billingservice.application.ports.out.PurchaseOrderRepoistoryPort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.PurchaseOrderStatus;
import com.example.billingservice.domain.enums.SequenceNumberType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.PurchaseOrder;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.infrastructure.out.persistance.mapper.PurchaseOrderMapper;
import com.example.billingservice.shared.ParseEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService implements PurchaseOrderUseCase {

    private final PurchaseOrderRepoistoryPort purchaseOrderRepoistoryPort;
    private final UploadDocumentService uploadDocumentService;
    private final GenerateInvoiceNumberUseCase generateInvoiceNumberUseCase;
    private final PartnerUseCase partnerUseCase;
    private final PurchaseOrderMapper purchaseOrderMapper;

    @Override
    public Page<PurchaseOrderPageItemDTO> getPurchaseOrders(String keyword, String filtre, int page) {
        PurchaseOrderStatus status = ParseEnum.parseEnum(filtre, PurchaseOrderStatus.class);
        return  purchaseOrderRepoistoryPort.findAllPurchaseOrders(keyword,status, page);
    }

    @Override
    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderCreateDTO purchaseOrderCreateDTO) throws IOException {

        if(purchaseOrderRepoistoryPort.existsByPurchaseOrderNumber(purchaseOrderCreateDTO.getPurchaseOrderNumber())){
            throw BillingException.alreadyExists(
                    "PurchaseOrder", "PurchaseOrderNumber", purchaseOrderCreateDTO.getPurchaseOrderNumber()
            );
        }

        if(!partnerUseCase.customerExistsByIdPartner(UUID.fromString(purchaseOrderCreateDTO.getPartner()))) {
            throw BillingException.notFound("Partner", purchaseOrderCreateDTO.getPartner());
        }

        String purchaseOrderNumber = generateInvoiceNumberUseCase.generate(SequenceNumberType.PURCHASE_ORDER);
        purchaseOrderCreateDTO.setPurchaseOrderNumber(purchaseOrderNumber);

        Document purchaseOrderDocument = null;
        if (purchaseOrderCreateDTO.getPurchaseOrderDocument() != null && !purchaseOrderCreateDTO.getPurchaseOrderDocument().isEmpty()) {
            UploadedFile document = new UploadedFile(
                    purchaseOrderCreateDTO.getPurchaseOrderDocument().getOriginalFilename(),
                    purchaseOrderCreateDTO.getPurchaseOrderDocument().getContentType(),
                    purchaseOrderCreateDTO.getPurchaseOrderDocument().getBytes()
            );

            purchaseOrderDocument = uploadDocumentService.upload(
                    purchaseOrderCreateDTO.getPurchaseOrderNumber(),
                    DocumentType.PURCHASE_ORDER,
                    document
            );
        }

        PurchaseOrder purchaseOrder = purchaseOrderMapper.purchaseOrderCreateDTOtoDomain(
                purchaseOrderCreateDTO, purchaseOrderDocument
        );

/*
        SyncInvoiceItems.syncInvoiceItems(
                invoice,
                createDTO.getInvoiceItems() != null ? createDTO.getInvoiceItems() : List.of()
        );
*/

        PurchaseOrder savedPurchaseOrder = purchaseOrderRepoistoryPort.createPurchaseOrder(purchaseOrder);

        generateInvoiceNumberUseCase.validateNextSequence(SequenceNumberType.PURCHASE_ORDER, purchaseOrderNumber);


        return purchaseOrderMapper.domainToPurchaseOrderDTO(savedPurchaseOrder);
    }

    @Override
    public PurchaseOrder getById(UUID idPurchaseOrder) {
        if(!purchaseOrderRepoistoryPort.existsByPurchaseOrderId(idPurchaseOrder)){
            throw BillingException.notFound("PurchaseOrder", String.valueOf(idPurchaseOrder));
        }
        return purchaseOrderRepoistoryPort.getById(idPurchaseOrder);
    }

    @Override
    public List<PurchaseOrderSummaryDTO> getPurchaseOrderSummary() {
        return purchaseOrderRepoistoryPort.getPurchaseOrderSummary();
    }

    @Override
    public void deletePurchaseOrder(UUID idPurchaseOrder) {
        if(!purchaseOrderRepoistoryPort.existsByPurchaseOrderId(idPurchaseOrder)){
            throw BillingException.notFound("PurchaseOrder", String.valueOf(idPurchaseOrder));
        }
        purchaseOrderRepoistoryPort.delete(idPurchaseOrder);
    }

    @Override
    public PurchaseOrderDTO updatePurchaseOrder(PurchaseOrderUpdateDTO purchaseOrderUpdateDTO) throws IOException {
        if(purchaseOrderUpdateDTO.getIdPurchaseOrder() != null && !existsByPurchaseOrderId(purchaseOrderUpdateDTO.getIdPurchaseOrder())){
            throw BillingException.notFound("Bon de commande", String.valueOf(purchaseOrderUpdateDTO.getIdPurchaseOrder()));
        }

        PurchaseOrder purchaseOrder = getById(purchaseOrderUpdateDTO.getIdPurchaseOrder());

        if(purchaseOrderUpdateDTO.getPurchaseOrderNumber() != null
                && !purchaseOrderUpdateDTO.getPurchaseOrderNumber().equals(purchaseOrder.getReference())){
            throw BillingException.badRequest("Impossible de mettre à jour le numéro de document");
        }

        UploadedFile document = new UploadedFile(
                purchaseOrderUpdateDTO.getPurchaseOrderDocument().getOriginalFilename(),
                purchaseOrderUpdateDTO.getPurchaseOrderDocument().getContentType(),
                purchaseOrderUpdateDTO.getPurchaseOrderDocument().getBytes()
        );


        Document invoiceDocument =
                uploadDocumentService.upload(purchaseOrder.getReference(), DocumentType.PURCHASE_ORDER, document);

        purchaseOrderUpdateDTO.setPartner(String.valueOf(purchaseOrder.getPartner().getIdPartner()));


        PurchaseOrder purchaseOrder1 = purchaseOrderMapper.updateDTOtoDomain(purchaseOrderUpdateDTO, purchaseOrder, invoiceDocument);


/*        SyncInvoiceItems.syncInvoiceItems(
                invoice,
                invoiceUpdateDTO.getInvoiceItems() != null ? invoiceUpdateDTO.getInvoiceItems() : List.of()
        );*/


        return purchaseOrderRepoistoryPort.update(purchaseOrder1);
    }

    @Override
    public PurchaseOrderDTO updatePurchaseOrderStatus(UUID invoiceId, PurchaseOrderStatus purchaseOrderStatus) {
        return purchaseOrderRepoistoryPort.updateStatus(invoiceId,purchaseOrderStatus);
    }

    @Override
    public boolean existsByPurchaseOrderNumber(String purchaseOrderNumber) {
        return purchaseOrderRepoistoryPort.existsByPurchaseOrderNumber(purchaseOrderNumber);
    }

    @Override
    public boolean existsByPurchaseOrderId(UUID purchaseOrderId) {
        return purchaseOrderRepoistoryPort.existsByPurchaseOrderId(purchaseOrderId);
    }
}
