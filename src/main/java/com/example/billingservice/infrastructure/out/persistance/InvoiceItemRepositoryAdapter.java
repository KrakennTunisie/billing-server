package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.InvoiceItemRepositoryPort;
import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceItemEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.InvoiceItemMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.InvoiceItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class InvoiceItemRepositoryAdapter implements InvoiceItemRepositoryPort {

    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceItemMapper invoiceItemMapper;
    @Override
    public InvoiceItem getById(UUID idInvoiceItem) {
        InvoiceItemEntity invoiceItemEntity = invoiceItemRepository.getReferenceById(idInvoiceItem);
        return invoiceItemMapper.toInvoiceItem(invoiceItemEntity);
    }

    @Override
    public InvoiceItemEntity getInvoiceItemEntityById(UUID invoiceItemId) {
        return invoiceItemRepository.getReferenceById(invoiceItemId);
    }
}
