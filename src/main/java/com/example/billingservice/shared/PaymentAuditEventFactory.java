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
public class PaymentAuditEventFactory {

    private static final String SOURCE_SERVICE = "billing-service";

    // private final AuditActorProvider auditActorProvider;

    public AuditEvent paymentCreated(
            UUID correlationId,
            String paymentId,
            Map<String, Object> payment,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "PAYMENT_CREATED",
                "Payment",
                paymentId,
                null,
                payment,
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent paymentUpdated(
            UUID correlationId,
            String paymentId,
            Map<String, Object> before,
            Map<String, Object> after,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "PAYMENT_UPDATED",
                "Payment",
                paymentId,
                before,
                after,
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent paymentStatusChanged(
            UUID correlationId,
            String paymentId,
            String previousStatus,
            String newStatus,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "PAYMENT_STATUS_CHANGED",
                "Payment",
                paymentId,
                Map.of("status", previousStatus),
                Map.of("status", newStatus),
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent paymentCompleted(
            UUID correlationId,
            String paymentId,
            String previousStatus,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "PAYMENT_COMPLETED",
                "Payment",
                paymentId,
                Map.of("status", previousStatus),
                Map.of("status", "COMPLETED"),
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent paymentFailed(
            UUID correlationId,
            String paymentId,
            String previousStatus,
            String failureReason,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "PAYMENT_FAILED",
                "Payment",
                paymentId,
                Map.of("status", previousStatus),
                Map.of("status", "FAILED"),
                false,
                failureReason,
                enversRevision
        );
    }

    public AuditEvent paymentRefunded(
            UUID correlationId,
            String paymentId,
            String previousStatus,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "PAYMENT_REFUNDED",
                "Payment",
                paymentId,
                Map.of("status", previousStatus),
                Map.of("status", "REFUNDED"),
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent paymentDeleted(
            UUID correlationId,
            String paymentId,
            Map<String, Object> payment,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "PAYMENT_DELETED",
                "Payment",
                paymentId,
                payment,
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
