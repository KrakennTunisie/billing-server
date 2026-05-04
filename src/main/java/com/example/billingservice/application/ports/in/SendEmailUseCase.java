package com.example.billingservice.application.ports.in;

import com.example.billingservice.infrastructure.out.persistance.dto.SendEmailRequest;

import java.util.UUID;

public interface SendEmailUseCase {
    void sendInvoiceEmail(UUID invoiceId, SendEmailRequest request);

    void sendEmail(SendEmailRequest request);

}
