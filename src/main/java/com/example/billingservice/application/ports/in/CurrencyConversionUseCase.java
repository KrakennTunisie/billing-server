package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.model.ExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CurrencyConversionUseCase {

    ExchangeRate convert(
            BigDecimal amount,
            String fromCurrency,
            String toCurrency,
            LocalDate date
    );
}
