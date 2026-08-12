package com.example.billingservice.infrastructure.out.messaging;

import com.example.billingservice.application.ports.out.AuditEventPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditEventPublisher implements AuditEventPublisherPort {

    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;

    @Value("${kafka.topic.audit-event:kerp.audit.event}")
    private String topic;

    @Override
    public void publish(AuditEvent auditEvent) {
        kafkaTemplate.send(topic, String.valueOf(auditEvent.eventId()), auditEvent);
    }
}
