package com.example.billingservice.shared;

import com.example.billingservice.infrastructure.out.messaging.AuditEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreditNoteAuditEventFactory {

    private static final String SOURCE_SERVICE = "billing-service";

    // private final AuditActorProvider auditActorProvider;

    public AuditEvent creditNoteCreated(
            UUID correlationId,
            String creditNoteId,
            Map<String, Object> creditNote,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "CREDIT_NOTE_CREATED",
                "CreditNote",
                creditNoteId,
                null,
                creditNote,
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent creditNoteUpdated(
            UUID correlationId,
            String creditNoteId,
            Map<String, Object> before,
            Map<String, Object> after,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "CREDIT_NOTE_UPDATED",
                "CreditNote",
                creditNoteId,
                before,
                after,
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent creditNoteStatusChanged(
            UUID correlationId,
            String creditNoteId,
            String previousStatus,
            String newStatus,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "CREDIT_NOTE_STATUS_CHANGED",
                "CreditNote",
                creditNoteId,
                Map.of("status", previousStatus),
                Map.of("status", newStatus),
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent creditNoteCancelled(
            UUID correlationId,
            String creditNoteId,
            String previousStatus,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "CREDIT_NOTE_CANCELLED",
                "CreditNote",
                creditNoteId,
                Map.of("status", previousStatus),
                Map.of("status", "CANCELLED"),
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent creditNoteDeleted(
            UUID correlationId,
            String creditNoteId,
            Map<String, Object> creditNote,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "CREDIT_NOTE_DELETED",
                "CreditNote",
                creditNoteId,
                creditNote,
                null,
                true,
                null,
                enversRevision
        );
    }

    private AuditEvent base(
            UUID correlationId,
            String action,
            String resourceType,
            String resourceId,
            Map<String, Object> before,
            Map<String, Object> after,
            boolean success,
            String failureReason,
            Long enversRevision
    ) {

        // AuditActor auditActor = auditActorProvider.getCurrentActor();

        return new AuditEvent(
                UUID.randomUUID(),
                correlationId,
                Instant.now(),
                SOURCE_SERVICE,
                /* auditActor.userId() */ "714165ba-4115-45f2-a5d4-6240d521c6f3",
                /* auditActor.firstName() */ "Wassef",
                /* auditActor.lastName() */ "Ammar",
                /* auditActor.roles() */ List.of("Admin"),
                action,
                resourceType,
                resourceId,
                before,
                after,
                success ? "SUCCESS" : "FAILURE",
                failureReason,
                /*ipAddress*/ "xxx",
                enversRevision
        );
    }
}
