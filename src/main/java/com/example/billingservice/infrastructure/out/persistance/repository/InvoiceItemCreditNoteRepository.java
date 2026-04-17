package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCreditNoteItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceItemCreditNoteRepository extends JpaRepository<InvoiceCreditNoteItemEntity, UUID> {
}
