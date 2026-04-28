package com.example.billingservice.infrastructure.out.persistance.projections;

import com.example.billingservice.domain.enums.InvoiceCurrency;

import java.math.BigDecimal;
import java.util.Date;

public interface PartnerInvoiceAmountStatsProjection {
    InvoiceCurrency getInvoiceCurrency();
    Double getAppliedExchangeRate();
    Date getExchangeRateReferenceDate();
    BigDecimal getTotalAmount();
    BigDecimal getPendingAmount();
}
