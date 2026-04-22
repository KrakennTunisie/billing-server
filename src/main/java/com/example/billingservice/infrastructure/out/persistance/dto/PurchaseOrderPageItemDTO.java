package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.InvoiceCurrency;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder
public class PurchaseOrderPageItemDTO {

    private UUID idPurchaseOrder;
    private String purchaseOrderNumber;
    private Date issueDate;
    private InvoiceCurrency purchaseCurrency;
    private Double totalExclTaxEUR;
    private Double totalInclTaxEUR;
    private Double totalExclTaxTND;
    private Double totalInclTaxTND;

    private Double totalExclTaxUSD;
    private Double totalInclTaxUSD;

    private Double vatRate;
    private Double appliedExchangeRate;
    private PartnerSummaryDTO partner;
}
