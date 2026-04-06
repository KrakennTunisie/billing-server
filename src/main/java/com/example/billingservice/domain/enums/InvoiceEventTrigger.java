package com.example.billingservice.domain.enums;

public enum InvoiceEventTrigger {
    USER,           // action utilisateur (backoffice / frontend)
    SYSTEM,         // traitement interne automatique
    TTN,
    OTHER
}
