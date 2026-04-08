package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.enums.SequenceNumberType;

public interface GenerateInvoiceNumberUseCase {
    String generate(SequenceNumberType sequenceNumberType);
    void validateNextSequence(SequenceNumberType sequenceNumberType,String invoiceNumber);
}
