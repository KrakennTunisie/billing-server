package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.PurchaseOrder;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderPageItemDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface PurchaseOrderRepoistoryPort {

    Page<PurchaseOrderPageItemDTO> findAllPurchaseOrders(String keyword , int page);


    PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder);
    PurchaseOrder getById(UUID idPurchaseOrder);

    PurchaseOrderDTO update(PurchaseOrder purchaseOrder);

    void delete(UUID idPurchaseOrder);

    boolean existsByPurchaseOrderNumber(String purchaseOrderNumber);

    boolean existsByPurchaseOrderId(UUID purchaseOrderId);
}
