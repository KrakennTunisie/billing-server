package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.Invoice;

public interface InvoiceUseCase {
    Invoice createInvoice(Invoice invoice);
}
