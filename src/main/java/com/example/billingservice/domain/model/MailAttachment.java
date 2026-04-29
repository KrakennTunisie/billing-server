package com.example.billingservice.domain.model;

public record MailAttachment(
        String filename,
        String contentType,
        byte[] content
) {}
