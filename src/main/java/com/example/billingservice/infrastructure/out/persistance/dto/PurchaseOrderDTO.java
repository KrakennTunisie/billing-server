package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.*;
import com.example.billingservice.domain.model.PurchaseOrderItem;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class PurchaseOrderDTO {

    private UUID idPurchaseOrder;
    private String purchaseOrderNumber;
    private PurchaseOrderStatus purchaseOrderStatus;
    private Date issueDate;
    private InvoiceCurrency purchaseCurrency;
    private Double totalExclTaxEUR;
    private Double totalInclTaxEUR;
    private Double totalExclTaxTND;
    private Double totalInclTaxTND;

    private Double totalExclTaxUSD;
    private Double totalInclTaxUSD;

    private Double vatRate;
    private PaymentMethod paymentMethod;
    private PaymentCondition paymentCondition;
    private Double appliedExchangeRate;
    private PurchaseOrderType purchaseOrderType;
    private PartnerSummaryDTO partner;

    private List<InvoiceSummaryDTO> invoices;

    private List<PurchaseOrderItem> purchaseOrderItems;

    private DocumentSummaryDTO purchaseOrderDocument;
}
