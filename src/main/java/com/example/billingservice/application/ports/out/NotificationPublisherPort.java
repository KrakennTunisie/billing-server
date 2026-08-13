package com.example.billingservice.application.ports.out;

import com.example.billingservice.infrastructure.out.messaging.NotificationEvent;

public interface NotificationPublisherPort {
    void publish(NotificationEvent notificationEvent);
}
