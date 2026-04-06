package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.InvoiceCounter;

import java.util.Optional;

public interface InvoiceCounterRepositoryPort {
    Optional<InvoiceCounter> findByYearForUpdate(Integer year);

    InvoiceCounter save(InvoiceCounter invoiceCounter);
}
