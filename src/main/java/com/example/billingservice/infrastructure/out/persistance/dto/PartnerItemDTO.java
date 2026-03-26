package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.PartnerType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class PartnerItemDTO {

    private UUID idPartner;

    private String name;

    private String email;

    private String phoneNumber;

    private String taxRegistrationNumber;

    private PartnerType partnerType;

    private String country;

    private String address;

    private String iban;
}
