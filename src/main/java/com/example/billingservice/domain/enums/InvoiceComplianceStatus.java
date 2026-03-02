package com.example.billingservice.domain.enums;

public enum InvoiceComplianceStatus {
    RECEIVED,
    SIGNING_PENDING,
    SIGNING_FAILED,
    SIGNING_SUCCEEDED,
    TTN_PENDING,
    TTN_SUBMITTED,
    TTN_ACCEPTED,
    TTN_REJECTED,
    COMPLETED,
    FAILED,
    CANCELLED,
}
