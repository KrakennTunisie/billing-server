package com.example.billingservice.infrastructure.out.persistance.projections;

public interface PartnerInvoiceCountStatsProjection {
    Long getTotalInvoices();
    Long getPaidInvoices();
    Long getPendingInvoices();
}
