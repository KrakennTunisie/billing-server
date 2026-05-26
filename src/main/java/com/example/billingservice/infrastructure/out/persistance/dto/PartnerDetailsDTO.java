package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.PartnerType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class PartnerDetailsDTO {

    private UUID idPartner;
    private String name;
    private String email;
    private String phoneNumber;
    private String taxRegistrationNumber;
    private String country;
    private String address;
    private String iban;
    private PartnerType partnerType;
    private DocumentSummaryDTO rne;
    private DocumentSummaryDTO contract;
    private DocumentSummaryDTO patente;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
