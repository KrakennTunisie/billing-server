package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.entity.PurchaseOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaPurchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, UUID> {
}
