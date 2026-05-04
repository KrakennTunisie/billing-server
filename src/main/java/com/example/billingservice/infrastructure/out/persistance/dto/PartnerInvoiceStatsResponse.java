package com.example.billingservice.infrastructure.out.persistance.dto;

import java.math.BigDecimal;

public record PartnerInvoiceStatsResponse(
        BigDecimal totalAmount,
        Long totalInvoices,
        Long paidInvoices,
        Long pendingInvoices,
        BigDecimal pendingAmount,
        BigDecimal averageInvoice) {}
