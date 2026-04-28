package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.PurchaseOrderStatus;
import com.example.billingservice.domain.model.PurchaseOrder;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PurchaseOrderUseCase {

        /***Client **/
        Page<PurchaseOrderPageItemDTO> getClientPurchaseOrders(String keyword , String filtre, int page);

        PurchaseOrderDTO createClientPurchaseOrder(PurchaseOrderCreateDTO purchaseOrderCreateDTO) throws IOException;

        PurchaseOrderDTO getClientPurchaseOrderById(UUID idPurchaseOrder);

        PurchaseOrder getDomainePurchaseOrderById(UUID idPurchaseOrder);
        List<PurchaseOrderSummaryDTO> getClientPurchaseOrderSummary();

        void deleteClientPurchaseOrder(UUID idPurchaseOrder);


        PurchaseOrderDTO updateClientPurchaseOrder(PurchaseOrderUpdateDTO purchaseOrderUpdateDTO) throws IOException;
        PurchaseOrderDTO updateClientPurchaseOrderStatus(UUID invoiceId, PurchaseOrderStatus purchaseOrderStatus);

        boolean existsByPurchaseOrderNumber(String purchaseOrderNumber);

        boolean existsByClientPurchaseOrderId(UUID purchaseOrderId);

        /** Supplier**/
        Page<PurchaseOrderPageItemDTO> getSupplierPurchaseOrders(String keyword , String filtre, int page);

        PurchaseOrderDTO createSupplierPurchaseOrder(PurchaseOrderCreateDTO purchaseOrderCreateDTO) throws IOException;

        PurchaseOrder getSupplierPurchaseOrderById(UUID idPurchaseOrder);
        List<PurchaseOrderSummaryDTO> getSupplierPurchaseOrderSummary();

        void deleteSupplierPurchaseOrder(UUID idPurchaseOrder);


        PurchaseOrderDTO updateSupplierPurchaseOrder(PurchaseOrderUpdateDTO purchaseOrderUpdateDTO) throws IOException;
        PurchaseOrderDTO updateSupplierPurchaseOrderStatus(UUID invoiceId, PurchaseOrderStatus purchaseOrderStatus);

        boolean existsBySupplierPurchaseOrderNumber(String purchaseOrderNumber);

        boolean existsBySupplierPurchaseOrderId(UUID purchaseOrderId);

}
