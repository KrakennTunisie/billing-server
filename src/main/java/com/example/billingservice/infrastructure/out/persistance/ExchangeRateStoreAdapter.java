package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.ExchangeRateStorePort;
import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.model.ExchangeRate;
import com.example.billingservice.infrastructure.out.persistance.entity.ExchangeRateEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.ExchangeRateMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.ExchangeRateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ExchangeRateStoreAdapter implements ExchangeRateStorePort {

    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateMapper exchangeRateMapper;

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        ExchangeRateEntity saved = exchangeRateRepository.save(
                exchangeRateMapper.toEntity(exchangeRate)
        );

        ExchangeRateEntity savedInversion = exchangeRateRepository.save(
                exchangeRateMapper.toInvertedEntity(exchangeRate)
        );

        return exchangeRateMapper.toDomain(saved);
    }

    @Override
    public Optional<ExchangeRate> findByCurrenciesAndDate(
            InvoiceCurrency fromCurrency,
            InvoiceCurrency toCurrency,
            LocalDate date
    ) {
        return exchangeRateRepository
                .findByFromCurrencyAndToCurrencyAndRateDate(fromCurrency, toCurrency, date)
                .map(exchangeRateMapper::toDomain);
    }
}
