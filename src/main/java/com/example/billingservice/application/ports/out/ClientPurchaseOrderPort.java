package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.enums.PurchaseOrderStatus;
import com.example.billingservice.domain.model.PurchaseOrder;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderPageItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderPartnerSummaryDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderSummaryDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ClientPurchaseOrderPort {

    Page<PurchaseOrderPageItemDTO> findAllPurchaseOrders(String keyword , PurchaseOrderStatus status, int page);

    PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder);

    PurchaseOrderDTO getById(UUID idPurchaseOrder);

    PurchaseOrder getDomainePurchaseOrderById(UUID idPurchaseOrder);

    PurchaseOrderDTO update(PurchaseOrder purchaseOrder);

    PurchaseOrderDTO updateStatus(UUID purchaseOrderId, PurchaseOrderStatus newStatus);

    void delete(UUID idPurchaseOrder);

    boolean existsByPurchaseOrderNumber(String purchaseOrderNumber);

    boolean existsByPurchaseOrderId(UUID purchaseOrderId);

    List<PurchaseOrderSummaryDTO> getPurchaseOrderSummary();

    Page<PurchaseOrderPageItemDTO> getPurchaseOrderByClientId(UUID idClient, int page);

}
