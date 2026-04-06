package com.example.billingservice.infrastructure.out.persistance.dto;

public record CurrencyTotals(
        Double totalExclTaxEUR,
        Double totalInclTaxEUR,
        Double totalExclTaxTND,
        Double totalInclTaxTND) {
}
