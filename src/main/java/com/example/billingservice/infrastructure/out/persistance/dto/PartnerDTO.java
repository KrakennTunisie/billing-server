package com.example.billingservice.infrastructure.out.persistance.dto;


import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.enums.PaymentCondition;
import com.example.billingservice.domain.model.Address;
import com.example.billingservice.domain.model.AuditLog;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.Invoice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Schema(description = "Données du partenaire")
public class PartnerDTO {


    private UUID idPartner;

    private boolean active;

    private boolean enablePortal;

    private String partnerName;
    private String maritalStatus;
    private String companyName;
    private String displayName;
    private String email;
    private String personnelPhoneNumber;
    private String professionnalPhoneNumber;
    private Address billingAddress;
    private Address shippingAddress;
    private String Language;

    private InvoiceCurrency currency;
    private String taxRate;
    private String taxRegistrationNumber;
    private String paymentCondition;
    private String iban;

    private DocumentSummaryDTO rne;
    private DocumentSummaryDTO contract;
    private DocumentSummaryDTO patente;
    private PartnerType partnerType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
