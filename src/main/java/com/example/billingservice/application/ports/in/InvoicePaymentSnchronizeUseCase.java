package com.example.billingservice.application.ports.in;

import java.util.UUID;

public interface InvoicePaymentSnchronizeUseCase {
    boolean validatePaymentAmount(UUID invoiceId, Double amount);

    void validatePayment(UUID invoiceId, Double amount);
}
