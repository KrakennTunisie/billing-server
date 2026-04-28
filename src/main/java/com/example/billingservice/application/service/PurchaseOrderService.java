package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.GenerateInvoiceNumberUseCase;
import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.application.ports.in.PurchaseOrderUseCase;
import com.example.billingservice.application.ports.out.ClientPurchaseOrderPort;
import com.example.billingservice.application.ports.out.PurchaseOrderRepoistoryPort;
import com.example.billingservice.application.ports.out.SupplierPurchaseOrderPort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.PurchaseOrderStatus;
import com.example.billingservice.domain.enums.SequenceNumberType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.PurchaseOrder;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.infrastructure.out.persistance.mapper.PurchaseOrderMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.SupplierPurchaseOrderRepository;
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

    private final ClientPurchaseOrderPort clientPurchaseOrderPort;
    private final SupplierPurchaseOrderPort supplierPurchaseOrderPort;
    private final UploadDocumentService uploadDocumentService;
    private final GenerateInvoiceNumberUseCase generateInvoiceNumberUseCase;
    private final PartnerUseCase partnerUseCase;
    private final PurchaseOrderMapper purchaseOrderMapper;

    /** Client Purchase Order  **/

    @Override
    public Page<PurchaseOrderPageItemDTO> getClientPurchaseOrders(String keyword, String filtre, int page) {
        PurchaseOrderStatus status = ParseEnum.parseEnum(filtre, PurchaseOrderStatus.class);
        return  clientPurchaseOrderPort.findAllPurchaseOrders(keyword,status, page);
    }

    @Override
    public PurchaseOrderDTO createClientPurchaseOrder(PurchaseOrderCreateDTO purchaseOrderCreateDTO) throws IOException {

        if(clientPurchaseOrderPort.existsByPurchaseOrderNumber(purchaseOrderCreateDTO.getPurchaseOrderNumber())){
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
                    purchaseOrderCreateDTO.getPurchaseOrderDocument().getInputStream(),
                    purchaseOrderCreateDTO.getPurchaseOrderDocument().getSize()
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
        System.out.println(purchaseOrder.getPurchaseOrderType());
        PurchaseOrder savedPurchaseOrder = clientPurchaseOrderPort.createPurchaseOrder(purchaseOrder);

        generateInvoiceNumberUseCase.validateNextSequence(SequenceNumberType.PURCHASE_ORDER, purchaseOrderNumber);


        return purchaseOrderMapper.domainToPurchaseOrderDTO(savedPurchaseOrder);
    }

    @Override
    public PurchaseOrder getClientPurchaseOrderById(UUID idPurchaseOrder) {
        if(!clientPurchaseOrderPort.existsByPurchaseOrderId(idPurchaseOrder)){
            throw BillingException.notFound("PurchaseOrder", String.valueOf(idPurchaseOrder));
        }
        return clientPurchaseOrderPort.getById(idPurchaseOrder);
    }

    @Override
    public List<PurchaseOrderSummaryDTO> getClientPurchaseOrderSummary() {
        return clientPurchaseOrderPort.getPurchaseOrderSummary();
    }

    @Override
    public void deleteClientPurchaseOrder(UUID idPurchaseOrder) {
        if(!clientPurchaseOrderPort.existsByPurchaseOrderId(idPurchaseOrder)){
            throw BillingException.notFound("PurchaseOrder", String.valueOf(idPurchaseOrder));
        }
        clientPurchaseOrderPort.delete(idPurchaseOrder);
    }

    @Override
    public PurchaseOrderDTO updateClientPurchaseOrder(PurchaseOrderUpdateDTO purchaseOrderUpdateDTO) throws IOException {
        if(purchaseOrderUpdateDTO.getIdPurchaseOrder() != null && !existsByClientPurchaseOrderId(purchaseOrderUpdateDTO.getIdPurchaseOrder())){
            throw BillingException.notFound("Bon de commande", String.valueOf(purchaseOrderUpdateDTO.getIdPurchaseOrder()));
        }

        PurchaseOrder purchaseOrder = getClientPurchaseOrderById(purchaseOrderUpdateDTO.getIdPurchaseOrder());

        if(purchaseOrderUpdateDTO.getPurchaseOrderNumber() != null
                && !purchaseOrderUpdateDTO.getPurchaseOrderNumber().equals(purchaseOrder.getReference())){
            throw BillingException.badRequest("Impossible de mettre à jour le numéro de document");
        }

        UploadedFile document = new UploadedFile(
                purchaseOrderUpdateDTO.getPurchaseOrderDocument().getOriginalFilename(),
                purchaseOrderUpdateDTO.getPurchaseOrderDocument().getContentType(),
                purchaseOrderUpdateDTO.getPurchaseOrderDocument().getInputStream(),
                purchaseOrderUpdateDTO.getPurchaseOrderDocument().getSize()
        );


        Document invoiceDocument =
                uploadDocumentService.upload(purchaseOrder.getReference(), DocumentType.PURCHASE_ORDER, document);

        purchaseOrderUpdateDTO.setPartner(String.valueOf(purchaseOrder.getPartner().getIdPartner()));


        PurchaseOrder purchaseOrder1 = purchaseOrderMapper.updateDTOtoDomain(purchaseOrderUpdateDTO, purchaseOrder, invoiceDocument);


/*        SyncInvoiceItems.syncInvoiceItems(
                invoice,
                invoiceUpdateDTO.getInvoiceItems() != null ? invoiceUpdateDTO.getInvoiceItems() : List.of()
        );*/

        System.out.println(purchaseOrder1);
        return clientPurchaseOrderPort.update(purchaseOrder1);
    }

    @Override
    public PurchaseOrderDTO updateClientPurchaseOrderStatus(UUID invoiceId, PurchaseOrderStatus purchaseOrderStatus) {
        return clientPurchaseOrderPort.updateStatus(invoiceId,purchaseOrderStatus);
    }

    @Override
    public boolean existsByPurchaseOrderNumber(String purchaseOrderNumber) {
        return clientPurchaseOrderPort.existsByPurchaseOrderNumber(purchaseOrderNumber);
    }

    @Override
    public boolean existsByClientPurchaseOrderId(UUID purchaseOrderId) {
        return clientPurchaseOrderPort.existsByPurchaseOrderId(purchaseOrderId);
    }

    /** Supplier purchaseOrder **/

    @Override
    public Page<PurchaseOrderPageItemDTO> getSupplierPurchaseOrders(String keyword, String filtre, int page) {
        PurchaseOrderStatus status = ParseEnum.parseEnum(filtre, PurchaseOrderStatus.class);
        return  supplierPurchaseOrderPort.findAllPurchaseOrders(keyword,status, page);
    }

    @Override
    public PurchaseOrderDTO createSupplierPurchaseOrder(PurchaseOrderCreateDTO purchaseOrderCreateDTO) throws IOException {
        if(supplierPurchaseOrderPort.existsByPurchaseOrderNumber(purchaseOrderCreateDTO.getPurchaseOrderNumber())){
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
                    purchaseOrderCreateDTO.getPurchaseOrderDocument().getInputStream(),
                    purchaseOrderCreateDTO.getPurchaseOrderDocument().getSize()
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

        PurchaseOrder savedPurchaseOrder = supplierPurchaseOrderPort.createPurchaseOrder(purchaseOrder);

        generateInvoiceNumberUseCase.validateNextSequence(SequenceNumberType.PURCHASE_ORDER, purchaseOrderNumber);


        return purchaseOrderMapper.domainToPurchaseOrderDTO(savedPurchaseOrder);
    }

    @Override
    public PurchaseOrder getSupplierPurchaseOrderById(UUID idPurchaseOrder) {
        if(!supplierPurchaseOrderPort.existsByPurchaseOrderId(idPurchaseOrder)){
            throw BillingException.notFound("PurchaseOrder", String.valueOf(idPurchaseOrder));
        }
        return supplierPurchaseOrderPort.getById(idPurchaseOrder);
    }

    @Override
    public List<PurchaseOrderSummaryDTO> getSupplierPurchaseOrderSummary() {
        return supplierPurchaseOrderPort.getPurchaseOrderSummary();
    }

    @Override
    public void deleteSupplierPurchaseOrder(UUID idPurchaseOrder) {
        if(!supplierPurchaseOrderPort.existsByPurchaseOrderId(idPurchaseOrder)){
            throw BillingException.notFound("PurchaseOrder", String.valueOf(idPurchaseOrder));
        }
        supplierPurchaseOrderPort.delete(idPurchaseOrder);
    }

    @Override
    public PurchaseOrderDTO updateSupplierPurchaseOrder(PurchaseOrderUpdateDTO purchaseOrderUpdateDTO) throws IOException {
        if(purchaseOrderUpdateDTO.getIdPurchaseOrder() != null && !existsBySupplierPurchaseOrderId(purchaseOrderUpdateDTO.getIdPurchaseOrder())){
            throw BillingException.notFound("Bon de commande", String.valueOf(purchaseOrderUpdateDTO.getIdPurchaseOrder()));
        }

        PurchaseOrder purchaseOrder = getSupplierPurchaseOrderById(purchaseOrderUpdateDTO.getIdPurchaseOrder());

        if(purchaseOrderUpdateDTO.getPurchaseOrderNumber() != null
                && !purchaseOrderUpdateDTO.getPurchaseOrderNumber().equals(purchaseOrder.getReference())){
            throw BillingException.badRequest("Impossible de mettre à jour le numéro de document");
        }

        UploadedFile document = new UploadedFile(
                purchaseOrderUpdateDTO.getPurchaseOrderDocument().getOriginalFilename(),
                purchaseOrderUpdateDTO.getPurchaseOrderDocument().getContentType(),
                purchaseOrderUpdateDTO.getPurchaseOrderDocument().getInputStream(),
                purchaseOrderUpdateDTO.getPurchaseOrderDocument().getSize()
        );


        Document invoiceDocument =
                uploadDocumentService.upload(purchaseOrder.getReference(), DocumentType.PURCHASE_ORDER, document);

        purchaseOrderUpdateDTO.setPartner(String.valueOf(purchaseOrder.getPartner().getIdPartner()));


        PurchaseOrder purchaseOrder1 = purchaseOrderMapper.updateDTOtoDomain(purchaseOrderUpdateDTO, purchaseOrder, invoiceDocument);

        return supplierPurchaseOrderPort.update(purchaseOrder1);
    }

    @Override
    public PurchaseOrderDTO updateSupplierPurchaseOrderStatus(UUID invoiceId, PurchaseOrderStatus purchaseOrderStatus) {
        return supplierPurchaseOrderPort.updateStatus(invoiceId,purchaseOrderStatus);
    }

    @Override
    public boolean existsBySupplierPurchaseOrderNumber(String purchaseOrderNumber) {
        return supplierPurchaseOrderPort.existsByPurchaseOrderNumber(purchaseOrderNumber);
    }

    @Override
    public boolean existsBySupplierPurchaseOrderId(UUID purchaseOrderId) {
        return supplierPurchaseOrderPort.existsByPurchaseOrderId(purchaseOrderId);
    }
}
