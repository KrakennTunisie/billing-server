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

    @Query("SELECT p FROM SupplierEntity p WHERE " +
            "(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.taxRegistrationNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:country IS NULL OR LOWER(p.country) = LOWER(:country))")
    Page<SupplierEntity> findSuppliers(
            @Param("keyword") String keyword,
            @Param("country") String country,
            Pageable pageable);
}
