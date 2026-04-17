package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceItemEntity;

import java.util.UUID;

public interface InvoiceItemRepositoryPort {

    InvoiceItem getById(UUID idInvoiceItem);

    InvoiceItemEntity getInvoiceItemEntityById(UUID invoiceItemId);

}
