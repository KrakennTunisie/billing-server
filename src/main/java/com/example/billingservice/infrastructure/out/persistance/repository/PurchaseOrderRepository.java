package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.entity.PurchaseOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, UUID> {

    boolean existsByIdPurchaseOrder(UUID idPurchaseOrder);

    boolean existsByReference(String reference);

    @Query("""
        SELECT p FROM PurchaseOrderEntity p
        WHERE
            (
                :keyword IS NULL OR :keyword = '' OR
                LOWER(p.reference) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(p.partner.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(p.partner.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
        """)
    Page<PurchaseOrderEntity> getPurchaseOrders(
            @Param("keyword") String keyword,
            Pageable pageable
    );

}
