package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.ExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CurrencyConversionUseCase {

    ExchangeRate convert(
            String fromCurrency,
            String toCurrency,
            LocalDate date
    );
}
