package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.entity.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaPartnerRepository extends JpaRepository<PartnerEntity, UUID> {


    Optional<PartnerEntity> findByName(String name);

    Optional <PartnerEntity> findByEmail(String email);
    Optional <PartnerEntity> findByTaxRegistrationNumber (String TaxRegistrationNumber);

     List<PartnerEntity> findByPartnerType(PartnerType partnerType);


}
