package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.CurrencyConversionUseCase;
import com.example.billingservice.application.ports.out.ExchangeRatePort;
import com.example.billingservice.application.ports.out.ExchangeRateStorePort;
import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.model.ExchangeRate;
import com.example.billingservice.shared.ParseEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CurrencyConversionService implements CurrencyConversionUseCase {

    private final ExchangeRatePort exchangeRatePort;
    private final ExchangeRateStorePort exchangeRateStorePort;

    @Override
    public ExchangeRate convert(
            BigDecimal amount,
            String fromCurrency,
            String toCurrency,
            LocalDate date
    ) {

        InvoiceCurrency from = ParseEnum.parseEnum(fromCurrency, InvoiceCurrency.class);
        InvoiceCurrency to = ParseEnum.parseEnum(toCurrency, InvoiceCurrency.class);

        ExchangeRate exchangeRate = exchangeRateStorePort
                .findByCurrenciesAndDate(from, to, date)
                .orElseGet(() -> {
                    ExchangeRate fetched = exchangeRatePort.fetchExchangeRate(fromCurrency, toCurrency, date);
                    return exchangeRateStorePort.save(fetched);
                });

        return exchangeRate;
    }
}
