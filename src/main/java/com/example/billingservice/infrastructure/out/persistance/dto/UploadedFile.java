package com.example.billingservice.infrastructure.out.persistance.dto;

public record UploadedFile(        String originalFileName,
                                   String mimeType,
                                   byte[] content) {
}
