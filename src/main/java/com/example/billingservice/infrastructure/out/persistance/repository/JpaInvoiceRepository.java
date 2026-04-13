package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.InvoiceType;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface JpaInvoiceRepository extends JpaRepository<InvoiceEntity, UUID> {
    @Query("""
SELECT i FROM InvoiceEntity i
WHERE
    (
        :keyword IS NULL OR :keyword = '' OR
        LOWER(i.reference) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(i.partner.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(i.partner.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
AND
    (
        :status IS NULL OR i.invoiceStatus = :status
    )
AND
    (
        :type IS NULL OR i.invoiceType = :type
    )
""")
    Page<InvoiceEntity> getInvoices(
            @Param("keyword") String keyword,
            @Param("status") InvoiceStatus status,
            @Param("type") InvoiceType type,
            Pageable pageable
    );

    boolean existsByReference(String invoiceNumber);

    boolean existsByIdInvoice(UUID invoiceId);
}
