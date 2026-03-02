package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaInvoiceItemRepository extends JpaRepository<InvoiceItemEntity, UUID> {
}
