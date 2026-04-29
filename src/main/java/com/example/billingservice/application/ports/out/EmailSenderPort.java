package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.MailJob;

public interface EmailSenderPort {
    void sendEmail(MailJob job);
}
