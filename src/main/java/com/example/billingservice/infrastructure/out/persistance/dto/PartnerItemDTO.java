package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.model.Address;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class PartnerItemDTO {

    private UUID idPartner;

    private String partnerName;

    private String companyName;

    private String email;

    private String professionnalPhoneNumber;

    private String taxRegistrationNumber;

    private PartnerType partnerType;

    private Address billingAddress;

    private String iban;
}
