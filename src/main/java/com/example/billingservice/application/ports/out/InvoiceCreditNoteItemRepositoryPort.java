package com.example.billingservice.application.ports.out;

import java.util.UUID;

public interface InvoiceCreditNoteItemRepositoryPort {
    int getTotalCreditedQuantityInvoiceItem(UUID invoiceItemID);
}
