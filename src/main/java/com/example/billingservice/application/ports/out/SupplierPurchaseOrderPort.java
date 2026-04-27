package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.enums.PurchaseOrderStatus;
import com.example.billingservice.domain.model.PurchaseOrder;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderPageItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderSummaryDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface SupplierPurchaseOrderPort {

    Page<PurchaseOrderPageItemDTO> findAllPurchaseOrders(String keyword , PurchaseOrderStatus status, int page);


    PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder);

    PurchaseOrder getById(UUID idPurchaseOrder);

    PurchaseOrderDTO update(PurchaseOrder purchaseOrder);

    PurchaseOrderDTO updateStatus(UUID purchaseOrderId, PurchaseOrderStatus newStatus);

    void delete(UUID idPurchaseOrder);

    boolean existsByPurchaseOrderNumber(String purchaseOrderNumber);

    boolean existsByPurchaseOrderId(UUID purchaseOrderId);

    public List<PurchaseOrderSummaryDTO> getPurchaseOrderSummary();
}
