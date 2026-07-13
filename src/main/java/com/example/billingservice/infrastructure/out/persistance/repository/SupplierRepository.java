package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.dto.PartnerSummaryDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.CustomerEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.SupplierEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<SupplierEntity, UUID> {

    Optional<SupplierEntity> findByTaxRegistrationNumber(String taxRegistrationNumber);

    boolean existsByIdPartner(UUID idPartner);

    boolean existsByTaxRegistrationNumber(String taxRegistrationNumber);

    boolean existsByCompanyName(String companyName);

    SupplierEntity getSupplierByCompanyName(String companyName);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(p) > 0 FROM SupplierEntity p WHERE p.partnerName = :name")
    boolean existsByName(@Param("name") String name);

    Optional<SupplierEntity> findByPartnerName(String name);

    boolean existsByIban(String iban);

    SupplierEntity getSupplierEntityByEmail(String email);

    @Query("""
    SELECT p FROM SupplierEntity p
    WHERE
        (
            :keyword IS NULL OR :keyword = '' OR
            LOWER(p.partnerName) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR
            LOWER(p.companyName) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR
            LOWER(p.displayName) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR
            LOWER(p.email) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR
            LOWER(p.taxRegistrationNumber) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
        )
    AND
        (
            :country IS NULL OR :country = '' OR
            LOWER(p.billingAddressEntity.region) = LOWER(CAST(:country AS string)) OR
            LOWER(p.shippingAddressEntity.region) = LOWER(CAST(:country AS string))
        )
""")
    Page<SupplierEntity> findSuppliers(
            @Param("keyword") String keyword,
            @Param("country") String country,
            Pageable pageable
    );

    Optional<SupplierEntity> findByEmail(String email);

    @Query("""
    SELECT p FROM SupplierEntity p
    WHERE
        (
            :keyword IS NULL OR :keyword = '' OR
            LOWER(p.partnerName) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR
            LOWER(p.companyName) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR
            LOWER(p.displayName) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR
            LOWER(p.email) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR
            LOWER(p.taxRegistrationNumber) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
        )
    AND
        (
            :country IS NULL OR :country = '' OR
            LOWER(p.billingAddressEntity.region) = LOWER(CAST(:country AS string)) OR
            LOWER(p.shippingAddressEntity.region) = LOWER(CAST(:country AS string))
        )
""")
    List<SupplierEntity> getSuppliers(
            @Param("keyword") String keyword,
            @Param("country") String country
    );
}
