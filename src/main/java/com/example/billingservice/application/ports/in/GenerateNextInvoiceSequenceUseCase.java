package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.enums.SequenceNumberType;

public interface GenerateNextInvoiceSequenceUseCase {
    Long generateNextSequence(SequenceNumberType sequenceNumberType, Integer year);
    void storeNextSequence(SequenceNumberType sequenceNumberType, Integer year, long nextSequence);
}
