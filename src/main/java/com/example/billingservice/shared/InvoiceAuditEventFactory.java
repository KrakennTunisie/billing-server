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
public class InvoiceAuditEventFactory {

    private static final String SOURCE_SERVICE = "billing-service";

    // private final AuditActorProvider auditActorProvider;

    public AuditEvent invoiceCreated(
            UUID correlationId,
            String invoiceId,
            Map<String, Object> invoice,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "INVOICE_CREATED",
                "Invoice",
                invoiceId,
                null,
                invoice,
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent invoiceUpdated(
            UUID correlationId,
            String invoiceId,
            Map<String, Object> before,
            Map<String, Object> after,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "INVOICE_UPDATED",
                "Invoice",
                invoiceId,
                before,
                after,
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent invoiceStatusChanged(
            UUID correlationId,
            String invoiceId,
            String previousStatus,
            String newStatus,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "INVOICE_STATUS_CHANGED",
                "Invoice",
                invoiceId,
                Map.of("status", previousStatus),
                Map.of("status", newStatus),
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent invoicePaid(
            UUID correlationId,
            String invoiceId,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "INVOICE_PAID",
                "Invoice",
                invoiceId,
                Map.of("status", "TO_PAY"),
                Map.of("status", "PAID"),
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent invoiceCancelled(
            UUID correlationId,
            String invoiceId,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "INVOICE_CANCELLED",
                "Invoice",
                invoiceId,
                Map.of("status", "TO_PAY"),
                Map.of("status", "CANCELLED"),
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent invoiceDeleted(
            UUID correlationId,
            String invoiceId,
            Map<String, Object> invoice,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "INVOICE_DELETED",
                "Invoice",
                invoiceId,
                invoice,
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