package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.infrastructure.out.persistance.entity.ClientInvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.SupplierInvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.projections.ClientInvoiceDashboardStatsProjection;
import com.example.billingservice.infrastructure.out.persistance.projections.PartnerInvoiceAmountStatsProjection;
import com.example.billingservice.infrastructure.out.persistance.projections.PartnerInvoiceCountStatsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface SupplierInvoicesRepository extends JpaRepository<SupplierInvoiceEntity, UUID> {

    @Query("""
SELECT i FROM SupplierInvoiceEntity i
WHERE
    (
        :keyword IS NULL OR :keyword = '' OR
        LOWER(i.reference) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(i.partner.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(i.partner.partnerName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
AND
(
    :status IS NOT NULL AND i.invoiceStatus = :status
    OR
    :status IS NULL AND i.invoiceStatus <> 'ARCHIVED'
)

""")
    Page<InvoiceEntity> getInvoices(
            @Param("keyword") String keyword,
            @Param("status") InvoiceStatus status,
            Pageable pageable
    );

    @Query("""
SELECT i FROM SupplierInvoiceEntity i
WHERE
    (
        i.partner.idPartner = :idPartner
    )

""")
    Page<InvoiceEntity> getClientInvoicesByPartner(
            @Param("idPartner") UUID idPartner,
            Pageable pageable
    );

    boolean existsByReference(String invoiceNumber);

    SupplierInvoiceEntity getSupplierInvoiceEntityByReference(String reference);

    boolean existsByIdInvoice(UUID invoiceId);

    SupplierInvoiceEntity getSupplierInvoiceEntityByIdInvoice(UUID idInvoice);


    @Query("""
    SELECT
        COUNT(i) as totalInvoices,
        SUM(CASE WHEN i.invoiceStatus = com.example.billingservice.domain.enums.InvoiceStatus.PAID THEN 1 ELSE 0 END) as paidInvoices,
        SUM(CASE WHEN i.invoiceStatus = :pendingStatus THEN 1 ELSE 0 END) as pendingInvoices
    FROM SupplierInvoiceEntity i
    WHERE i.partner.idPartner = :partnerId
      AND i.invoiceStatus NOT IN (
          com.example.billingservice.domain.enums.InvoiceStatus.DRAFT,
          com.example.billingservice.domain.enums.InvoiceStatus.CANCELLED
      )
    """)
    PartnerInvoiceCountStatsProjection getPartnerInvoiceCountStats(
            @Param("partnerId") UUID partnerId,
            @Param("pendingStatus") InvoiceStatus pendingStatus
    );


    @Query("""
        SELECT
            i.currency as invoiceCurrency,
            i.appliedExchangeRate as appliedExchangeRate,
            i.exchangeRateReferenceDate as exchangeRateReferenceDate,
            COALESCE(SUM(it.totalPriceIncTax), 0) as totalAmount,
            COALESCE(SUM(
                CASE
                    WHEN i.invoiceStatus = :pendingStatus
                    THEN it.totalPriceIncTax
                    ELSE 0
                END
            ), 0) as pendingAmount
        FROM SupplierInvoiceEntity i
        JOIN i.invoiceItems it
        WHERE i.partner.idPartner = :partnerId
          AND i.invoiceStatus NOT IN (
              com.example.billingservice.domain.enums.InvoiceStatus.DRAFT,
              com.example.billingservice.domain.enums.InvoiceStatus.CANCELLED
          )
        GROUP BY i.currency, i.appliedExchangeRate, i.exchangeRateReferenceDate
        """)
    List<PartnerInvoiceAmountStatsProjection> getPartnerInvoiceAmountStatsGroupedByCurrency(
            @Param("partnerId") UUID partnerId,
            @Param("pendingStatus") InvoiceStatus pendingStatus
    );



    @Query("""
    SELECT
        c.idPartner AS id,
        c.partnerName AS client,

        COALESCE(SUM(it.totalPriceIncTax), 0) AS amount,

        MONTH(i.issueDate) AS month,

        i.currency AS invoiceCurrency,
        i.appliedExchangeRate AS appliedExchangeRate,
        i.exchangeRateReferenceDate AS exchangeRateReferenceDate


    FROM SupplierInvoiceEntity i
    JOIN i.invoiceItems it
    JOIN i.partner c

    WHERE YEAR(i.issueDate) = :year
      AND i.invoiceStatus NOT IN (
        com.example.billingservice.domain.enums.InvoiceStatus.DRAFT,
        com.example.billingservice.domain.enums.InvoiceStatus.CANCELLED
    )

    GROUP BY
        c.idPartner,
        c.partnerName,
        MONTH(i.issueDate),
        i.currency,
        i.appliedExchangeRate,
        i.exchangeRateReferenceDate
""")
    List<ClientInvoiceDashboardStatsProjection> getAllClientInvoiceAmountStatsGroupedByCurrencyAndClientAndMonth(
            @Param("year") int year
    );


    @Query("""
    SELECT
        i.currency AS invoiceCurrency,
        i.appliedExchangeRate AS appliedExchangeRate,
        i.exchangeRateReferenceDate AS exchangeRateReferenceDate,

        COALESCE(SUM(it.totalPriceIncTax), 0) AS totalAmount,

        COALESCE(SUM(
            CASE
                WHEN i.invoiceStatus = :pendingStatus
                THEN it.totalPriceIncTax
                ELSE 0
            END
        ), 0) AS pendingAmount

    FROM SupplierInvoiceEntity i
    JOIN i.invoiceItems it

    WHERE i.invoiceStatus NOT IN (
        com.example.billingservice.domain.enums.InvoiceStatus.DRAFT,
        com.example.billingservice.domain.enums.InvoiceStatus.CANCELLED
    )

    GROUP BY
        i.currency,
        i.appliedExchangeRate,
        i.exchangeRateReferenceDate
""")
    List<PartnerInvoiceAmountStatsProjection> getAllClientInvoiceAmountStatsGroupedByCurrency(
            @Param("pendingStatus") InvoiceStatus pendingStatus
    );

    @Query("""
    SELECT
        COUNT(i) AS totalInvoices,

        COALESCE(SUM(
            CASE
                WHEN i.invoiceStatus = com.example.billingservice.domain.enums.InvoiceStatus.PAID
                THEN 1
                ELSE 0
            END
        ), 0) AS paidInvoices,

        COALESCE(SUM(
            CASE
                WHEN i.invoiceStatus = :pendingStatus
                THEN 1
                ELSE 0
            END
        ), 0) AS pendingInvoices

    FROM SupplierInvoiceEntity i

    WHERE i.invoiceStatus NOT IN (
        com.example.billingservice.domain.enums.InvoiceStatus.DRAFT,
        com.example.billingservice.domain.enums.InvoiceStatus.CANCELLED
    )
""")
    PartnerInvoiceCountStatsProjection getAllClientInvoiceCountStats(
            @Param("pendingStatus") InvoiceStatus pendingStatus
    );

    List<SupplierInvoiceEntity> findTop3ByPartner_IdPartnerAndInvoiceStatusNotInOrderByIssueDateDesc(
            UUID supplierId,
            Collection<InvoiceStatus> excludedStatuses
    );


    @Query("""
    SELECT i FROM SupplierInvoiceEntity i
    WHERE
        i.partner.idPartner = :clientId
    AND
        i.createdAt >= :dateDebut
    AND
        i.createdAt <= :dateFin
    ORDER BY i.createdAt DESC
""")
    List<SupplierInvoiceEntity> getSupplierInvoicesByPeriod(
            @Param("clientId") UUID clientId,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin
    );

    @Query("""
    SELECT i FROM SupplierInvoiceEntity i
    WHERE
        i.partner.idPartner = :supplierId
    AND
        i.createdAt >= :dateDebut
    AND
        i.createdAt <= :dateFin
    ORDER BY i.createdAt DESC
""")
    List<SupplierInvoiceEntity> getAllSupplierInvoicesByPeriod(
            @Param("clientId") UUID supplierId,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin
    );

    @Query("""
    SELECT i FROM SupplierInvoiceEntity i
    WHERE i.partner.idPartner = :supplierId
    ORDER BY i.issueDate DESC
""")
    List<SupplierInvoiceEntity> getAllSupplierInvoices(
            @Param("supplierId") UUID supplierId,
            Pageable pageable
    );

    @Query("""
    SELECT i FROM SupplierInvoiceEntity i
    WHERE i.invoiceStatus IN (
        com.example.billingservice.domain.enums.InvoiceStatus.PARTIALLY_PAID,
        com.example.billingservice.domain.enums.InvoiceStatus.TO_PAY
        )
    AND i.dueDate < :referenceDate
""")
    List<SupplierInvoiceEntity> getOverdueInvoices(
            @Param("referenceDate") Date referenceDate
    );
}
