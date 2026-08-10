package com.example.billingservice.infrastructure.out.persistance.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Builder
public record MailResultDTO(
         UUID eventId,
         String status,
         String errorMessage,
         Instant processedAt
) {
}
