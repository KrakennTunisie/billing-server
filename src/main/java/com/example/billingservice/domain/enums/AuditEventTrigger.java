package com.example.billingservice.domain.enums;

public enum AuditEventTrigger {
    USER,           // action utilisateur (backoffice / frontend)
    SYSTEM,         // traitement interne automatique
    TTN,
    OTHER
}
