package com.example.billingservice.infrastructure.out.persistance.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MailRequest(
         String eventId,
         String eventType,       // INVOICE_CREATED, PAYMENT_RECEIVED, etc.
         String toEmail,
         String subject,
         String body,
         List<MailAttachementMetadata>attachments,
         LocalDateTime occurredAt
) {
}
