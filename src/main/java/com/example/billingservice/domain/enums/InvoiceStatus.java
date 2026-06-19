package com.example.billingservice.domain.enums;

public enum InvoiceStatus {
    DRAFT,
    TO_PAY,
    TO_COLLECT,
    PARTIALLY_PAID,
    PAID,
    OVERDUE,
    CANCELLED
}
