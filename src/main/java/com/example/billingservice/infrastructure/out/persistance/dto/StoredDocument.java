package com.example.billingservice.infrastructure.out.persistance.dto;


public record StoredDocument(
        String fileName,
        String mimeType,
        String storageUrl,
        String hash
) {
}
