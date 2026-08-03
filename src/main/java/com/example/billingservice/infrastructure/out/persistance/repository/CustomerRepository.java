package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.entity.CustomerEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.SupplierEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> , PagingAndSortingRepository<CustomerEntity, UUID> {

    Optional<CustomerEntity> findByTaxRegistrationNumber(String taxRegistrationNumber);

    Optional<CustomerEntity> findByEmail(String email);

    boolean existsByIdPartner(UUID uuid);

    boolean existsByTaxRegistrationNumber(String taxRegistrationNumber);

    boolean existsByTaxRegistrationNumberAndIdPartnerNot(String taxRegistrationNumber, UUID idPartner);

    boolean existsByPartnerName(String name);

    boolean existsByCompanyName(String companyName);

    CustomerEntity getClientByCompanyName(String companyName);

    boolean existsByCompanyNameAndIdPartnerNot(String companyName, UUID idPartner);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdPartnerNot(String email, UUID idPartner);

    boolean existsByIban(String email);

    boolean existsByIbanAndIdPartnerNot(String iban, UUID idPartner);

    @Query("""
    SELECT p FROM CustomerEntity p
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
    Page<CustomerEntity> findCustomers(
            @Param("keyword") String keyword,
            @Param("country") String country,
            Pageable pageable);


    @Query("""
    SELECT p FROM CustomerEntity p
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
    List<CustomerEntity> getCustomers(
            @Param("keyword") String keyword,
            @Param("country") String country
            );
}
