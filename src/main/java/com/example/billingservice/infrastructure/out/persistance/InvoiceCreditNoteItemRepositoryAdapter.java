package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.InvoiceCreditNoteItemRepositoryPort;
import com.example.billingservice.infrastructure.out.persistance.repository.InvoiceItemCreditNoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class InvoiceCreditNoteItemRepositoryAdapter implements InvoiceCreditNoteItemRepositoryPort {

    private final InvoiceItemCreditNoteRepository invoiceItemCreditNoteRepository;
    @Override
    public int getTotalCreditedQuantityInvoiceItem(UUID invoiceItemID) {
        return invoiceItemCreditNoteRepository.getTotalCreditedQuantityByInvoiceItem(invoiceItemID);
    }
}
