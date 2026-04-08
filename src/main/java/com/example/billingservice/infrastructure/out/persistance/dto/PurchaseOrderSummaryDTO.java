package com.example.billingservice.infrastructure.out.persistance.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder
public class PurchaseOrderSummaryDTO {
    private UUID idPurchaseOrder;
    private String reference;
    private Date orderDate;
}
