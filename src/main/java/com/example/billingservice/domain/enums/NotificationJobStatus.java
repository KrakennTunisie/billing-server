package com.example.billingservice.domain.enums;

public enum NotificationJobStatus {
    PENDING,    // créé, pas encore relayé vers Kafka
    QUEUED,     // publié sur Kafka, en attente de traitement par mail-service
    SENT,       // confirmé envoyé par mail-service
    FAILED,     // échec définitif (retries épuisés)
    RETRYING    // échec temporaire, sera retenté
}
