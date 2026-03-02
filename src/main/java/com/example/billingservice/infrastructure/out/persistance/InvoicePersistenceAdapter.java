package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.InvoiceRepositoryPort;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.infrastructure.out.persistance.repository.JpaInvoiceRepository;

public class InvoicePersistenceAdapter implements InvoiceRepositoryPort {

    private final JpaInvoiceRepository jpaInvoiceRepository;

    public InvoicePersistenceAdapter(JpaInvoiceRepository jpaInvoiceRepository) {
        this.jpaInvoiceRepository = jpaInvoiceRepository;
    }

    @Override
    public Invoice save(Invoice invoice) {
        return null;
    }
}
