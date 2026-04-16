package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.GenerateInvoiceNumberUseCase;
import com.example.billingservice.application.ports.in.GenerateNextInvoiceSequenceUseCase;
import com.example.billingservice.domain.enums.SequenceNumberType;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class GenerateInvoiceNumberService implements GenerateInvoiceNumberUseCase {
    private final GenerateNextInvoiceSequenceUseCase generateNextInvoiceSequenceUseCase;

    public GenerateInvoiceNumberService(GenerateNextInvoiceSequenceUseCase generateNextInvoiceSequenceUseCase) {
        this.generateNextInvoiceSequenceUseCase = generateNextInvoiceSequenceUseCase;
    }

    @Override
    public String generate(SequenceNumberType sequenceNumberType) {
        int year = Year.now().getValue();
        long sequence = generateNextInvoiceSequenceUseCase.generateNextSequence(sequenceNumberType, year);

        return formatInvoiceNumber(sequenceNumberType, year, sequence);
    }

    @Override
    public void validateNextSequence(SequenceNumberType sequenceNumberType, String invoiceNumber) {
        int year = Year.now().getValue();
        long lastSequence = extractSequence(invoiceNumber);

         generateNextInvoiceSequenceUseCase.storeNextSequence(sequenceNumberType, year, lastSequence);
    }

    private String formatInvoiceNumber(SequenceNumberType sequenceNumberType, int year, long sequence) {

        return sequenceNumberType == SequenceNumberType.INVOICE
                ? "FAC-" + year + "-" + String.format("%05d", sequence)
                : sequenceNumberType == SequenceNumberType.CREDIT_NOTE
                    ? "AV-" + year + "-" + String.format("%05d", sequence)
                    : "BC-" + year + "-" + String.format("%05d", sequence);
    }



    private long extractSequence(String invoiceNumber) {
        String[] parts = invoiceNumber.split("-");
        return Long.parseLong(parts[2]);
    }
}
