package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.InvoiceCurrency;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ExchangeRate {

    private UUID idExchangeRate;
    private InvoiceCurrency fromCurrency;
    private InvoiceCurrency toCurrency;
    private BigDecimal quote;
    private LocalDate rateDate;          // business date of the rate
    private LocalDateTime fetchedAt;     // when API returned / when we fetched it
    private String source;               // exchangerate-api, ECB, etc.
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
