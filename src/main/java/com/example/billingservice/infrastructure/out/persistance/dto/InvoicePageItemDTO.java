package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder
public class InvoicePageItemDTO {
    private UUID idInvoice;
    private String invoiceNumber;
    private Date issueDate;
    private Date dueDate;
    private InvoiceType invoiceType;
    private InvoiceStatus invoiceStatus;
    private InvoiceComplianceStatus invoiceComplianceStatus;
    private InvoiceCurrency invoiceCurrency;
    private Double totalExclTaxEUR;
    private Double totalInclTaxEUR;
    private Double totalExclTaxTND;
    private Double totalInclTaxTND;
    private Double vatRate;
    private Double appliedExchangeRate;
    private PurchaseOrderSummaryDTO purchaseOrder;
    private PartnerSummaryDTO partner;
}
