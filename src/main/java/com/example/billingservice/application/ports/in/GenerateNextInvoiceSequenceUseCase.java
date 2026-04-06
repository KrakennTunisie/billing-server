package com.example.billingservice.application.ports.in;

public interface GenerateNextInvoiceSequenceUseCase {
    Long generateNextSequence(Integer year);
    void storeNextSequence(Integer year, long nextSequence);
}
