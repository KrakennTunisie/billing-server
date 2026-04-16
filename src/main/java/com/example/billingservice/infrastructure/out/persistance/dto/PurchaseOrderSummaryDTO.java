package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.InvoiceCurrency;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder
public class PurchaseOrderSummaryDTO {
    private UUID idPurchaseOrder;
    private String purchaseOrderNumber;
    private Date issueDate;
    private InvoiceCurrency purchaseCurrency;
}
