package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceItemEntity;

import java.util.UUID;

public interface InvoiceItemUseCase {
    InvoiceItem getById(UUID invoiceItemId);

    InvoiceItemEntity getInvoiceItemEntityById(UUID invoiceItemId);
}
