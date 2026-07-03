package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.model.Address;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class PartnerSummaryDTO {
    private UUID idPartner;
    private String maritalStatus;
    private String partnerName;
    private String companyName;
    private String taxRegistrationNumber;
    private String email;
    private Address billingAddress;
    private String professionnalPhoneNumber;
    private InvoiceCurrency currency;
    private String taxRate;
    private PartnerType partnerType;

}
