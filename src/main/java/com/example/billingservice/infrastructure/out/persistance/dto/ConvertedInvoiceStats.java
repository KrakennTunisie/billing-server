package com.example.billingservice.infrastructure.out.persistance.dto;

import java.math.BigDecimal;

public record ConvertedInvoiceStats(
        BigDecimal totalAmountTND,
        BigDecimal totalAmountEUR,
        BigDecimal totalAmountUSD,

        Long totalInvoices,
        Long paidInvoices,
        Long pendingInvoices,

        BigDecimal pendingAmountTND,
        BigDecimal pendingAmountEUR,
        BigDecimal pendingAmountUSD,

        BigDecimal averageInvoiceTND,
        BigDecimal averageInvoiceEUR,
        BigDecimal averageInvoiceUSD
) {}
