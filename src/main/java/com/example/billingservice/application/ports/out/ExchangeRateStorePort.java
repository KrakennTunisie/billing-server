package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.model.ExchangeRate;

import java.time.LocalDate;
import java.util.Optional;

public interface ExchangeRateStorePort {

    ExchangeRate save(ExchangeRate exchangeRate);
    Optional<ExchangeRate> findByCurrenciesAndDate(InvoiceCurrency fromCurrency, InvoiceCurrency toCurrency, LocalDate date);
}
