package com.example.billingservice.infrastructure.out.persistance.dto;

import java.util.UUID;

public record MailJobAttachmentRequest(
        UUID attachmentRequestId,
        String fileName,
        String filePath
) {
}
