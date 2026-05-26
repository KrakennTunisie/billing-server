package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.CreditNoteSynchronizationUseCase;
import com.example.billingservice.application.ports.out.InvoiceCreditNoteItemRepositoryPort;
import com.example.billingservice.application.ports.out.InvoiceItemRepositoryPort;
import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceItemEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CreditNoteSynchronizationService implements CreditNoteSynchronizationUseCase {

    private final InvoiceCreditNoteItemRepositoryPort invoiceCreditNoteItemRepositoryPort;
    private final InvoiceItemRepositoryPort invoiceItemRepositoryPort;

    @Override
    public boolean checkInvoiceItemAvailableQuantity(UUID invoiceItemId, int quantityToCredit){
        InvoiceItem invoiceItem =  invoiceItemRepositoryPort.getById(invoiceItemId);
        int totalInvoiceItemCreditedQuantity = invoiceCreditNoteItemRepositoryPort.getTotalCreditedQuantityInvoiceItem(invoiceItemId);
        return (totalInvoiceItemCreditedQuantity + quantityToCredit) <=  invoiceItem.getQuantity();
    }

    @Override
    public void synchronize (UUID invoiceItemId, int quantityToCredit){
        InvoiceItemEntity invoiceItemEntity =  invoiceItemRepositoryPort.getInvoiceItemEntityById(invoiceItemId);
        int totalInvoiceItemCreditedQuantity = invoiceCreditNoteItemRepositoryPort.getTotalCreditedQuantityInvoiceItem(invoiceItemId);
        invoiceItemEntity.setCreditedQuantity(totalInvoiceItemCreditedQuantity + quantityToCredit);
        invoiceItemRepositoryPort.saveInvoiceItem(invoiceItemEntity);
    }
}
