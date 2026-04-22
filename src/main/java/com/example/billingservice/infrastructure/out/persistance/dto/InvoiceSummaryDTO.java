package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.InvoiceComplianceStatus;
import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.InvoiceType;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Builder
@Getter
public class InvoiceSummaryDTO {

    private UUID idInvoice;

    private String invoiceNumber;

    private Date issueDate;

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

}
