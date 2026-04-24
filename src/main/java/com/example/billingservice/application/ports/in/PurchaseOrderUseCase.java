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
        Page<PurchaseOrderPageItemDTO> getPurchaseOrders(String keyword , String filtre, int page);

        PurchaseOrderDTO createPurchaseOrder(PurchaseOrderCreateDTO purchaseOrderCreateDTO) throws IOException;

        PurchaseOrder getById(UUID idPurchaseOrder);
        List<PurchaseOrderSummaryDTO> getPurchaseOrderSummary();

        void deletePurchaseOrder(UUID idPurchaseOrder);


        PurchaseOrderDTO updatePurchaseOrder(PurchaseOrderUpdateDTO purchaseOrderUpdateDTO) throws IOException;
        PurchaseOrderDTO updatePurchaseOrderStatus(UUID invoiceId, PurchaseOrderStatus purchaseOrderStatus);

        boolean existsByPurchaseOrderNumber(String purchaseOrderNumber);

        boolean existsByPurchaseOrderId(UUID purchaseOrderId);

}
