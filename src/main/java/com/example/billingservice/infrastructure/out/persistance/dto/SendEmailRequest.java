package com.example.billingservice.infrastructure.out.persistance.dto;

public record SendEmailRequest(
        String toEmail,
        String subject,
        String body
) {}
