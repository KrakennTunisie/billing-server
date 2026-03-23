package com.example.billingservice.infrastructure.out.persistance.dto;


import java.util.UUID;

public record StoredDocument(
        UUID idDocument,
        String fileName,
        String mimeType,
        String storageUrl,
        String hash
) {
}
