package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.model.ExchangeRate;
import com.example.billingservice.infrastructure.out.persistance.entity.ExchangeRateEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ExchangeRateMapper {

    public ExchangeRate toDomain(ExchangeRateEntity entity) {
        if (entity == null) {
            return null;
        }

        return ExchangeRate.builder()
                .idExchangeRate(entity.getIdExchangeRate())
                .fromCurrency(entity.getFromCurrency())
                .toCurrency(entity.getToCurrency())
                .quote(entity.getQuote())
                .rateDate(entity.getRateDate())
                .fetchedAt(entity.getFetchedAt())
                .source(entity.getSource())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public ExchangeRateEntity toEntity(ExchangeRate domain) {
        if (domain == null) {
            return null;
        }

        ExchangeRateEntity entity = new ExchangeRateEntity();
        entity.setIdExchangeRate(domain.getIdExchangeRate());
        entity.setFromCurrency(domain.getFromCurrency());
        entity.setToCurrency(domain.getToCurrency());
        entity.setQuote(domain.getQuote());
        entity.setRateDate(domain.getRateDate());
        entity.setFetchedAt(domain.getFetchedAt());
        entity.setSource(domain.getSource());
        return entity;
    }

    public ExchangeRateEntity toInvertedEntity(ExchangeRate domain) {
        if (domain == null) {
            return null;
        }

        ExchangeRateEntity entity = new ExchangeRateEntity();
        entity.setIdExchangeRate(domain.getIdExchangeRate());
        entity.setFromCurrency(domain.getToCurrency());
        entity.setToCurrency(domain.getFromCurrency());
        entity.setQuote(BigDecimal.ONE.divide(domain.getQuote(), RoundingMode.HALF_UP));
        entity.setRateDate(domain.getRateDate());
        entity.setFetchedAt(domain.getFetchedAt());
        entity.setSource(domain.getSource());
        return entity;
    }


}
