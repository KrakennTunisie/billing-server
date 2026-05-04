package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.infrastructure.out.persistance.entity.ClientInvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.projections.ClientInvoiceDashboardStatsProjection;
import com.example.billingservice.infrastructure.out.persistance.projections.PartnerInvoiceAmountStatsProjection;
import com.example.billingservice.infrastructure.out.persistance.projections.PartnerInvoiceCountStatsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ClientInvoicesRepository extends JpaRepository<ClientInvoiceEntity, UUID> {
    @Query("""
SELECT i FROM ClientInvoiceEntity i
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

""")
    Page<InvoiceEntity> getInvoices(
            @Param("keyword") String keyword,
            @Param("status") InvoiceStatus status,
            Pageable pageable
    );

    boolean existsByReference(String invoiceNumber);

    boolean existsByIdInvoice(UUID invoiceId);

    ClientInvoiceEntity getClientInvoiceEntityByIdInvoice(UUID idInvoice);

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
        FROM ClientInvoiceEntity i
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
        COUNT(i) as totalInvoices,
        SUM(CASE WHEN i.invoiceStatus = com.example.billingservice.domain.enums.InvoiceStatus.PAID THEN 1 ELSE 0 END) as paidInvoices,
        SUM(CASE WHEN i.invoiceStatus = :pendingStatus THEN 1 ELSE 0 END) as pendingInvoices
    FROM ClientInvoiceEntity i
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
        p.idPartner AS id,
        p.name AS client,
        i.appliedExchangeRate AS appliedExchangeRate,

        COALESCE(SUM(i.totalInclTaxTND), 0) AS amount,

        MONTH(i.issueDate) AS month,

        COUNT(DISTINCT i.idInvoice) AS totalInvoices

    FROM ClientInvoiceEntity i
    JOIN i.partner p

    WHERE YEAR(i.issueDate) = :year
      AND i.invoiceStatus NOT IN (
          com.example.billingservice.domain.enums.InvoiceStatus.DRAFT,
          com.example.billingservice.domain.enums.InvoiceStatus.CANCELLED
      )

    GROUP BY
        p.idPartner,
        p.name,
        MONTH(i.issueDate)

""")
    List<ClientInvoiceDashboardStatsProjection> getClientInvoicesDashboardStats(
            @Param("year") int year
    );

    @Query("""
    SELECT
        c.idPartner AS id,
        c.name AS client,

        COALESCE(SUM(it.totalPriceIncTax), 0) AS amount,

        MONTH(i.issueDate) AS month,

        i.currency AS invoiceCurrency,
        i.appliedExchangeRate AS appliedExchangeRate,
        i.exchangeRateReferenceDate AS exchangeRateReferenceDate


    FROM ClientInvoiceEntity i
    JOIN i.invoiceItems it
    JOIN i.partner c

    WHERE YEAR(i.issueDate) = :year
      AND i.invoiceStatus NOT IN (
        com.example.billingservice.domain.enums.InvoiceStatus.DRAFT,
        com.example.billingservice.domain.enums.InvoiceStatus.CANCELLED
    )

    GROUP BY
        c.idPartner,
        c.name,
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

    FROM ClientInvoiceEntity i
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

    FROM ClientInvoiceEntity i

    WHERE i.invoiceStatus NOT IN (
        com.example.billingservice.domain.enums.InvoiceStatus.DRAFT,
        com.example.billingservice.domain.enums.InvoiceStatus.CANCELLED
    )
""")
    PartnerInvoiceCountStatsProjection getAllClientInvoiceCountStats(
            @Param("pendingStatus") InvoiceStatus pendingStatus
    );
}
