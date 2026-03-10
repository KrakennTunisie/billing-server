package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.model.Partner;

import java.util.List;
import java.util.Optional;

public interface PartnerUseCase {

    Partner createPartner(PartnerDTO partner);
    Optional<Partner> getById(String id);
    List<Partner> getAll();
    Partner updatePartner (String id , PartnerDTO command);
    void deletePartner(String id);
    Partner getByName(String name);
    Partner getByEmail(String email);
    Partner getByTaxRegistrationNumber(String TaxRegistrationNumber);
    List <Partner> getByPartnerType(PartnerType partnerType);

}
