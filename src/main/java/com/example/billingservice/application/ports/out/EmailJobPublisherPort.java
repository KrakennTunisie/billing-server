package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.MailJob;

public interface EmailJobPublisherPort {
    void publish(MailJob job);

}
