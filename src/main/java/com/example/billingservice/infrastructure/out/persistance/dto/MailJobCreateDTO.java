package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.MailEventType;

public record MailJobCreateDTO(
        String eventId,
        String toEmail,
        String subject,
        String body,
        MailEventType eventType
) {
}
