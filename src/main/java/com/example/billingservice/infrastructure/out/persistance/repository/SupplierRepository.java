package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.entity.CustomerEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.SupplierEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<SupplierEntity, UUID> {
    Optional<SupplierEntity> findByTaxRegistrationNumber(String taxRegistrationNumber);
    boolean existsByTaxRegistrationNumber(String taxRegistrationNumber);

    @Query("""
    SELECT p FROM SupplierEntity p
    WHERE
        (
            :keyword IS NULL OR :keyword = '' OR
            LOWER(p.name) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR
            LOWER(p.email) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR
            LOWER(p.taxRegistrationNumber) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
        )
    AND
        (
            :country IS NULL OR :country = '' OR
            LOWER(p.country) = LOWER(CAST(:country AS string))
        )
""")
    Page<SupplierEntity> findSuppliers(
            @Param("keyword") String keyword,
            @Param("country") String country,
            Pageable pageable
    );
}
