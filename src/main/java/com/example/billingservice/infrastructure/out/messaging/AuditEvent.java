package com.example.billingservice.infrastructure.out.messaging;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record AuditEvent(
        UUID eventId,
        UUID correlationId,
        Instant timestamp,
        String sourceService,
        String actorUserId,
        String actorFirstName,
        String actorLastName,
        List<String> actorRoles,
        String action,
        String resourceType,
        String resourceId,
        Map<String, Object> beforeState,
        Map<String, Object> afterState,
        String outcome,
        String failureReason,
        String ipAddress,
        Long enversRevision
) {
}
