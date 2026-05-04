package com.example.billingservice.infrastructure.out.persistance.projections;

import com.example.billingservice.domain.enums.InvoiceCurrency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface ClientInvoiceDashboardStatsProjection {
    UUID getId();

    String getClient();

    BigDecimal getAmount();

    Integer getMonth();

    InvoiceCurrency getInvoiceCurrency();

    Double getAppliedExchangeRate();

    LocalDate getExchangeRateReferenceDate();


}
