package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.infrastructure.out.persistance.entity.ExchangeRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRateEntity, UUID> {
    Optional<ExchangeRateEntity> findByFromCurrencyAndToCurrencyAndRateDate(
            InvoiceCurrency fromCurrency,
            InvoiceCurrency toCurrency,
            LocalDate rateDate
    );
}
