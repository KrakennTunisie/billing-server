package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.PurchaseOrderItemRepositoryPort;
import com.example.billingservice.domain.model.PurchaseOrderItem;
import com.example.billingservice.infrastructure.out.persistance.entity.PurchaseOrderItemEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.PurchaseOrderItemMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.PurchaseOrderItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
@AllArgsConstructor
public class PurchaseOrderItemRepositoryAdapter implements PurchaseOrderItemRepositoryPort {

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final PurchaseOrderItemMapper purchaseOrderItemMapper;
    @Override
    public PurchaseOrderItem getById(UUID purchaseOrderItem) {
        PurchaseOrderItemEntity purchaseOrderItemEntity = purchaseOrderItemRepository.getReferenceById(purchaseOrderItem);
        return purchaseOrderItemMapper.toDomain(purchaseOrderItemEntity);
    }

    @Override
    public void updatedInvoicedQuantity(UUID purchaseOrderItemId, int invoicedQuantity) {

        PurchaseOrderItemEntity existingEntity = purchaseOrderItemRepository
                .findById(purchaseOrderItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item BC introuvable : " + purchaseOrderItemId));

        existingEntity.setInvoicedQuantity(invoicedQuantity);
        purchaseOrderItemRepository.save(existingEntity);
    }
}
