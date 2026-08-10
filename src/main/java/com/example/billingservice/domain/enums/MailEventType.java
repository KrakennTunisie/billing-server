package com.example.billingservice.domain.enums;

public enum MailEventType {
    // ===== Authentification / Utilisateurs (Keycloak) =====
    PASSWORD_RESET_OTP,           // OTP du stepper de reset password
    PASSWORD_CHANGED_CONFIRMATION,
    ACCOUNT_CREATED,
    ACCOUNT_ACTIVATION,
    ROLE_ASSIGNED,

    // ===== Factures client =====
    INVOICE_CREATED,
    INVOICE_SENT,
    INVOICE_OVERDUE,
    INVOICE_PAYMENT_REMINDER,
    INVOICE_CANCELLED,

    // ===== Avoirs (credit notes) =====
    CREDIT_NOTE_CREATED,
    CREDIT_NOTE_SENT,

    // ===== Factures fournisseur =====
    SUPPLIER_INVOICE_RECEIVED,
    SUPPLIER_INVOICE_VALIDATED,

    // ===== Bons de commande =====
    PURCHASE_ORDER_CREATED,
    PURCHASE_ORDER_APPROVED,
    PURCHASE_ORDER_REJECTED,

    // ===== Paiements =====
    PAYMENT_RECEIVED,
    PAYMENT_RECEIPT_GENERATED,
    PAYMENT_FAILED,



}
