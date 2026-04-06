package com.example.billingservice.application.ports.in;

public interface GenerateInvoiceNumberUseCase {
    String generate();
    void validateNextSequence(String invoiceNumber);
}
