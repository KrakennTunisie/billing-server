package com.example.billingservice.domain.model;

public record MailAttachment(
        String filename,
        String filePath,
        String contentType,
        byte[] content
) {}
