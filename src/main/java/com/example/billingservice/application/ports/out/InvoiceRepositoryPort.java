package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.Invoice;

public interface InvoiceRepositoryPort {

    Invoice save(Invoice invoice);
}
