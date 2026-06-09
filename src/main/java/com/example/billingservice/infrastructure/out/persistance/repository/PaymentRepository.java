package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.domain.enums.PaymentMethod;
import com.example.billingservice.infrastructure.out.persistance.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
    @Query("""
SELECT i FROM PaymentEntity  i

WHERE(
     :paymentMethod IS NULL OR i.method = :paymentMethod
)

AND
    (
        :keyword IS NULL OR :keyword = '' OR
        LOWER(i.reference) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(i.note) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(i.invoice.reference) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )

""")
    Page<PaymentEntity> getPayments(
            @Param("keyword") String keyword,
            @Param("paymentMethod") PaymentMethod paymentMethod,
            Pageable pageable
    );

    boolean existsByIdPayment(UUID idPayment);

    boolean existsByReference(String reference);

    @Query("""
SELECT i FROM PaymentEntity  i
where (
    i.invoice.idInvoice = :idInvoice
)
AND
    (
        :keyword IS NULL OR :keyword = '' OR
        LOWER(i.reference) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(i.note) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(i.invoice.reference) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )

""")
    Page<PaymentEntity> getPaymentsByInvoice(
            @Param("keyword") String keyword,
            @Param("idInvoice") UUID invoiceIdInvoice,
            Pageable pageable
    );


    @Query("""
SELECT i FROM PaymentEntity  i
where (
    i.invoice.partner.idPartner = :idPartner
)
AND
    (
        :keyword IS NULL OR :keyword = '' OR
        LOWER(i.reference) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(i.note) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(i.invoice.reference) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )

""")
    Page<PaymentEntity> getPaymentsByPartner(
            @Param("keyword") String keyword,
            @Param("idPartner") UUID idPartner,
            Pageable pageable
    );
}
