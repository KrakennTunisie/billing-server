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
public class PurchaseOrderAuditEventFactory {

    private static final String SOURCE_SERVICE = "billing-service";

    // private final AuditActorProvider auditActorProvider;

    public AuditEvent purchaseOrderCreated(
            UUID correlationId,
            String purchaseOrderId,
            Map<String, Object> purchaseOrder,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "PURCHASE_ORDER_CREATED",
                "PurchaseOrder",
                purchaseOrderId,
                null,
                purchaseOrder,
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent purchaseOrderUpdated(
            UUID correlationId,
            String purchaseOrderId,
            Map<String, Object> before,
            Map<String, Object> after,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "PURCHASE_ORDER_UPDATED",
                "PurchaseOrder",
                purchaseOrderId,
                before,
                after,
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent purchaseOrderStatusChanged(
            UUID correlationId,
            String purchaseOrderId,
            String previousStatus,
            String newStatus,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "PURCHASE_ORDER_STATUS_CHANGED",
                "PurchaseOrder",
                purchaseOrderId,
                Map.of("status", previousStatus),
                Map.of("status", newStatus),
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent purchaseOrderCancelled(
            UUID correlationId,
            String purchaseOrderId,
            String previousStatus,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "PURCHASE_ORDER_CANCELLED",
                "PurchaseOrder",
                purchaseOrderId,
                Map.of("status", previousStatus),
                Map.of("status", "CANCELLED"),
                true,
                null,
                enversRevision
        );
    }

    public AuditEvent purchaseOrderDeleted(
            UUID correlationId,
            String purchaseOrderId,
            Map<String, Object> purchaseOrder,
            Long enversRevision
    ) {

        return base(
                correlationId,
                "PURCHASE_ORDER_DELETED",
                "PurchaseOrder",
                purchaseOrderId,
                purchaseOrder,
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
                /*ipAddress*/ "xxxx",
                enversRevision
        );
    }
}
