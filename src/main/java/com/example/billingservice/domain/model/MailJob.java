package com.example.billingservice.domain.model;

import java.util.List;

public record MailJob(
        String to,
        String subject,
        String body,
        boolean html,
        List<MailAttachment> attachments) {
}
