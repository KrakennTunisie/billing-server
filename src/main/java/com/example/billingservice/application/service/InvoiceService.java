package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.application.ports.out.InvoiceRepositoryPort;
import com.example.billingservice.domain.model.Invoice;


public class InvoiceService implements InvoiceUseCase {

    private final InvoiceRepositoryPort invoiceRepositoryPort;

    public InvoiceService(InvoiceRepositoryPort invoiceRepositoryPort) {
        this.invoiceRepositoryPort = invoiceRepositoryPort;
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepositoryPort.save(invoice);
    }
}
