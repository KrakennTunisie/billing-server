package com.example.billingservice.infrastructure.out.persistance.dto;

import java.util.UUID;

public record DocumentReadFile(
        UUID idDocument,
        String fileName,
        String mimeType,
        byte[] content
) {
}
