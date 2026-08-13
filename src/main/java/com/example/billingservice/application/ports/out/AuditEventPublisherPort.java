package com.example.billingservice.application.ports.out;

import com.example.billingservice.infrastructure.out.messaging.AuditEvent;

public interface AuditEventPublisherPort {
    void publish(AuditEvent auditEvent);
}
