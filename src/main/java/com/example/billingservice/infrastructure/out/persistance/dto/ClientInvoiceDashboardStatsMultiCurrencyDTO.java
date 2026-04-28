package com.example.billingservice.infrastructure.out.persistance.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ClientInvoiceDashboardStatsMultiCurrencyDTO(
        UUID id,
        String client,
        BigDecimal amountTND,
        BigDecimal amountEUR,
        BigDecimal amountUSD,
        Integer month
) {
}
