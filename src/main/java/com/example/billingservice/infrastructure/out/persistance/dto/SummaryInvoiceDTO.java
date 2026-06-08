package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.InvoiceComplianceStatus;
import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.InvoiceType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Builder
public class SummaryInvoiceDTO {
    private UUID idInvoice;
    private String invoiceNumber;
    private Date issueDate;
    private Date dueDate;
    private InvoiceType invoiceType;
    private InvoiceStatus invoiceStatus;
    private InvoiceCurrency invoiceCurrency;
    private BigDecimal totalInclTax;
}
