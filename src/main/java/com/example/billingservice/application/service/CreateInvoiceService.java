package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.CreateInvoiceUseCase;
import com.example.billingservice.application.ports.out.InvoiceRepositoryPort;
import com.example.billingservice.domain.model.Invoice;

public class CreateInvoiceService implements CreateInvoiceUseCase {

    private final InvoiceRepositoryPort invoiceRepositoryPort;

    public CreateInvoiceService(InvoiceRepositoryPort invoiceRepositoryPort) {
        this.invoiceRepositoryPort = invoiceRepositoryPort;
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepositoryPort.save(invoice);
    }
}
