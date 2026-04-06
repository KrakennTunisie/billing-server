package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.PartnerType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class PartnerSummaryDTO {
    private UUID idPartner;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private PartnerType partnerType;

}
