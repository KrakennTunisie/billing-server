package com.example.billingservice.application.ports.in;

import java.math.BigDecimal;
import java.util.UUID;

public interface InvoicePaymentSnchronizeUseCase {
/*    boolean validatePaymentAmount(UUID invoiceId, Double amount);

    void validatePayment(UUID invoiceId, double amount);*/

    void applyPayment(UUID invoiceId, BigDecimal amount);
}
