package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.domain.enums.PurchaseOrderStatus;
import com.example.billingservice.infrastructure.out.persistance.entity.ClientPurchaseOrderEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.PurchaseOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ClientPurchaseOrderRepository extends JpaRepository<ClientPurchaseOrderEntity, UUID> {

    boolean existsByIdPurchaseOrder(UUID idPurchaseOrder);

    boolean existsByReference(String reference);

    @Query("""
        SELECT p FROM ClientPurchaseOrderEntity p
        WHERE
            (
                :keyword IS NULL OR :keyword = '' OR
                LOWER(p.reference) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(p.partner.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(p.partner.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
        AND
            (
        :status IS NULL OR p.purchaseOrderStatus = :status
             )
        """)
    Page<ClientPurchaseOrderEntity> getPurchaseOrders(
            @Param("keyword") String keyword,
            @Param("status") PurchaseOrderStatus status,
            Pageable pageable
    );
    @Query("SELECT p FROM ClientPurchaseOrderEntity p WHERE p.purchaseOrderStatus IN :statuses")
    List<ClientPurchaseOrderEntity> getPurchaseOrdersByStatus(@Param("statuses") List<PurchaseOrderStatus> statuses);
}
