package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.Invoice;

public interface CreateInvoiceUseCase {
    Invoice createInvoice(Invoice invoice);
}
