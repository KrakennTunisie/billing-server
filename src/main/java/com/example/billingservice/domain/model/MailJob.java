package com.example.billingservice.domain.model;

import java.util.List;
import java.util.UUID;

public record MailJob(
       // UUID uuid,
        String to,
        String subject,
        String body,
        boolean html,
        List<MailAttachment> attachments) {
}
