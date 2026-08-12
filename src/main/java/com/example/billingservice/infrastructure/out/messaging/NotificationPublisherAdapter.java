package com.example.billingservice.infrastructure.out.messaging;

import com.example.billingservice.application.ports.out.NotificationPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPublisherAdapter implements NotificationPublisherPort {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @Value("${mail.kafka.topic.notification-requested:kerp.notification.requested}")
    private String topic;

    @Override
    public void publish(NotificationEvent notificationEvent) {
        kafkaTemplate.send(topic, String.valueOf(notificationEvent.eventId()), notificationEvent);
    }

}
