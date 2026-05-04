package com.example.billingservice.infrastructure.out.persistance.projections;

import java.math.BigDecimal;

public interface InvoiceStatProjection {
    BigDecimal getTotalEur();
    BigDecimal getTotalTnd();
    Long getInvoiceCount();
}
