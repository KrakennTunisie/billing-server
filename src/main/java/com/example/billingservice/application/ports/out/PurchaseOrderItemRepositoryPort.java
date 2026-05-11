package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.PurchaseOrderItem;

import java.util.UUID;

public interface PurchaseOrderItemRepositoryPort {
    PurchaseOrderItem getById(UUID idInvoiceItem);
    void updatedInvoicedQuantity(UUID purchaseOrderItem, int invoicedQuantity);

}