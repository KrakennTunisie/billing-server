package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.PurchaseOrder;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.UUID;

public interface PurchaseOrderUseCase {
        Page<PurchaseOrderPageItemDTO> getPurchaseOrders(String keyword , int page);

        PurchaseOrderDTO createPurchaseOrder(PurchaseOrderCreateDTO purchaseOrderCreateDTO) throws IOException;

        PurchaseOrder getById(UUID idPurchaseOrder);

        void deletePurchaseOrder(UUID idPurchaseOrder);


        PurchaseOrderDTO updatePurchaseOrder(PurchaseOrderUpdateDTO purchaseOrderUpdateDTO) throws IOException;

        boolean existsByPurchaseOrderNumber(String purchaseOrderNumber);

        boolean existsByPurchaseOrderId(UUID purchaseOrderId);

}
