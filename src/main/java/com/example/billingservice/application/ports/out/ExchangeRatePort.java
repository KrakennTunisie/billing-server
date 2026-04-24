package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.ExchangeRate;

import java.time.LocalDate;

public interface ExchangeRatePort {
    ExchangeRate fetchExchangeRate(String fromCurrency, String toCurrency, LocalDate date);

}
