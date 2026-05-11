package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceItemEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.PurchaseOrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItemEntity, UUID> {
}
