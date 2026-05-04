package com.example.billingservice.infrastructure.out.persistance.projections;

import java.math.BigDecimal;

public interface PartnerInvoiceStatsProjection {
    BigDecimal getTotalAmount();
    Long getTotalInvoices();
    Long getPaidInvoices();
    Long getPendingInvoices();
    BigDecimal getPendingAmount();
    BigDecimal getAverageInvoice();
}
