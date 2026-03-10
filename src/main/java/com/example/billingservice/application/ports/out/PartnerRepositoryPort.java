package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.model.Partner;

import java.util.List;
import java.util.Optional;

public interface PartnerRepositoryPort {
    Partner save (Partner partner);
    Optional <Partner> findById(String id);
    List<Partner> findAll();
    void deleteById(String id);
    boolean existsById(String id);
    Partner findByName(String name);
    Partner findByEmail(String email);
    Partner findByTaxRegistrationNumber(String taxRegistrationNumber);
    List<Partner> findByPartnerType(PartnerType partnerType);
}
