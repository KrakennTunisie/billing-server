package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCreditNoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface InvoiceCreditNoteRepository extends JpaRepository<InvoiceCreditNoteEntity, UUID> {
    @Query("""
        SELECT cn FROM InvoiceCreditNoteEntity cn
        WHERE
            cn.invoice.idInvoice = :invoiceId
        AND
            (
                :keyword IS NULL OR :keyword = '' OR
                LOWER(cn.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(cn.motif) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
        AND
            (
                :status IS NULL OR cn.invoice.invoiceStatus = :status
            )
        """)
    Page<InvoiceCreditNoteEntity> getCreditNotesByInvoiceId(
            @Param("invoiceId") UUID invoiceId,
            @Param("keyword") String keyword,
            @Param("status") InvoiceCreditNoteStatus status,
            Pageable pageable
    );

    @Override
    boolean existsById(UUID uuid);

    boolean existsByInvoiceCreditNoteNumber(String invoiceCreditNoteNumber);

}
