package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.*;
import com.example.billingservice.domain.enums.PaymentCondition;
import com.example.billingservice.domain.model.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class InvoiceDTO {

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
    private Double totalExclTaxUSD;
    private Double totalInclTaxUSD;
    private Double remainingAmount;
    private Double vatRate;
    private String paymentCondition;
    private PaymentMethod paymentMethod;
    private Date exchangeRateReferenceDate;
    private Double appliedExchangeRate;
    private ExchangeRateSource exchangeRateSource;
    private String complianceQRcode;
    private PurchaseOrderSummaryDTO purchaseOrder;
    private PartnerSummaryDTO partner;
    private String comment;

    private List<InvoiceItem> invoiceItems;
    private boolean hasInvoiceCreditNotes;
    private DocumentSummaryDTO invoiceDocument;
}
