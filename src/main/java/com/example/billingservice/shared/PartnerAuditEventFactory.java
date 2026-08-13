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
public class PartnerAuditEventFactory {

    private static final String SOURCE_SERVICE = "billing-service";

    // private final AuditActorProvider auditActorProvider;

    // =========================
    // CLIENT
    // =========================

    public AuditEvent clientCreated(
            UUID correlationId,
            String clientId,
            Map<String, Object> client,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "CLIENT_CREATED",
                "Client",
                clientId,
                null,
                client,
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent clientUpdated(
            UUID correlationId,
            String clientId,
            Map<String, Object> before,
            Map<String, Object> after,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "CLIENT_UPDATED",
                "Client",
                clientId,
                before,
                after,
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent clientStatusChanged(
            UUID correlationId,
            String clientId,
            String previousStatus,
            String newStatus,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "CLIENT_STATUS_CHANGED",
                "Client",
                clientId,
                Map.of("status", previousStatus),
                Map.of("status", newStatus),
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent clientDeleted(
            UUID correlationId,
            String clientId,
            Map<String, Object> client,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "CLIENT_DELETED",
                "Client",
                clientId,
                client,
                null,
                true,
                null,
                enversRevision
        );
    }


    // =========================
    // SUPPLIER
    // =========================

    public AuditEvent supplierCreated(
            UUID correlationId,
            String supplierId,
            Map<String, Object> supplier,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "SUPPLIER_CREATED",
                "Supplier",
                supplierId,
                null,
                supplier,
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent supplierUpdated(
            UUID correlationId,
            String supplierId,
            Map<String, Object> before,
            Map<String, Object> after,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "SUPPLIER_UPDATED",
                "Supplier",
                supplierId,
                before,
                after,
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent supplierStatusChanged(
            UUID correlationId,
            String supplierId,
            String previousStatus,
            String newStatus,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "SUPPLIER_STATUS_CHANGED",
                "Supplier",
                supplierId,
                Map.of("status", previousStatus),
                Map.of("status", newStatus),
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent supplierDeleted(
            UUID correlationId,
            String supplierId,
            Map<String, Object> supplier,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "SUPPLIER_DELETED",
                "Supplier",
                supplierId,
                supplier,
                null,
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent clientDocumentAdded(
            UUID correlationId,
            String clientId,
            String documentId,
            Map<String, Object> document,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "CLIENT_DOCUMENT_ADDED",
                "Client",
                clientId,
                null,
                Map.of(
                        "documentId", documentId,
                        "document", document
                ),
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent supplierDocumentAdded(
            UUID correlationId,
            String supplierId,
            String documentId,
            Map<String, Object> document,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "SUPPLIER_DOCUMENT_ADDED",
                "Supplier",
                supplierId,
                null,
                Map.of(
                        "documentId", documentId,
                        "document", document
                ),
                true,
                null,
                enversRevision
        );
    }

    // =========================
    // BASE
    // =========================

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

                /* auditActor.userId() */
                "714165ba-4115-45f2-a5d4-6240d521c6f3",

                /* auditActor.firstName() */
                "Wassef",

                /* auditActor.lastName() */
                "Ammar",

                /* auditActor.roles() */
                List.of("Admin"),

                action,
                resourceType,
                resourceId,
                before,
                after,
                success ? "SUCCESS" : "FAILURE",
                failureReason,

                /* ipAddress */
                "xxx",

                enversRevision
        );
    }
}