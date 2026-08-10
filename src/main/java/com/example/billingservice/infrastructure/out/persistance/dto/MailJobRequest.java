package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.MailEventType;

import java.util.List;

public record MailJobRequest(
        String toEmail,
        String subject,
        String body,
        MailEventType eventType,
        List<MailJobAttachmentRequest> attachments
) {
}
