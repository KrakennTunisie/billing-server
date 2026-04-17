package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.InvoiceItemUseCase;
import com.example.billingservice.application.ports.out.InvoiceItemRepositoryPort;
import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceItemEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class InvoiceItemService  implements InvoiceItemUseCase {
    private final InvoiceItemRepositoryPort invoiceItemRepositoryPort;

    @Override
    public InvoiceItem getById(UUID invoiceItemId) {
        return invoiceItemRepositoryPort.getById(invoiceItemId);
    }

    @Override
    public InvoiceItemEntity getInvoiceItemEntityById(UUID invoiceItemId) {
        return invoiceItemRepositoryPort.getInvoiceItemEntityById(invoiceItemId);
    }
}
