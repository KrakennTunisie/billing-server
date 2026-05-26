package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCreditNoteItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface InvoiceItemCreditNoteRepository extends JpaRepository<InvoiceCreditNoteItemEntity, UUID> {

    @Query("""
    SELECT COALESCE(SUM(icnItem.quantity), 0)
    FROM InvoiceCreditNoteItemEntity icnItem
    WHERE icnItem.invoiceItem.idInvoiceItem = :invoiceItemId
    AND icnItem.invoiceCreditNote.invoiceCreditNoteStatus <> com.example.billingservice.domain.enums.InvoiceCreditNoteStatus.CANCELLED
    
""")
    Integer getTotalCreditedQuantityByInvoiceItem(
            @Param("invoiceItemId") UUID invoiceItemId
    );
}
