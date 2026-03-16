package com.example.billingservice.application.dto;

public record UploadedFile(        String originalFileName,
                                   String mimeType,
                                   byte[] content) {
}
