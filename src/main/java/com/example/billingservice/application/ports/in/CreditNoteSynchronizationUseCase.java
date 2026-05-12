package com.example.billingservice.application.ports.in;

import java.util.UUID;

public interface CreditNoteSynchronizationUseCase {

    boolean checkInvoiceItemAvailableQuantity(UUID invoiceItemId, int quantityToCredit);

    void synchronize (UUID invoiceItemId, int quantityToCredit);
}
