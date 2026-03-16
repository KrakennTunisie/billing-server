package com.example.billingservice.application.dto;


public record StoredDocument(
        String fileName,
        String mimeType,
        String storageUrl,
        String hash
) {
}
