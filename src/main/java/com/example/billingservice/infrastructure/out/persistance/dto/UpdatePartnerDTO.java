package com.example.billingservice.infrastructure.out.persistance.dto;
import com.example.billingservice.domain.enums.PartnerType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdatePartnerDTO {
    private String name;

    private String email;

    private String phoneNumber;

    private String taxRegistrationNumber;

    private String country;

    private String address;

    private String iban;

    private PartnerType partnerType;
}
