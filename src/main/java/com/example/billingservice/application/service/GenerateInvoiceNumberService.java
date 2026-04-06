package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.GenerateInvoiceNumberUseCase;
import com.example.billingservice.application.ports.in.GenerateNextInvoiceSequenceUseCase;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class GenerateInvoiceNumberService implements GenerateInvoiceNumberUseCase {
    private final GenerateNextInvoiceSequenceUseCase generateNextInvoiceSequenceUseCase;

    public GenerateInvoiceNumberService(GenerateNextInvoiceSequenceUseCase generateNextInvoiceSequenceUseCase) {
        this.generateNextInvoiceSequenceUseCase = generateNextInvoiceSequenceUseCase;
    }

    @Override
    public String generate() {
        int year = Year.now().getValue();
        long sequence = generateNextInvoiceSequenceUseCase.generateNextSequence(year);

        return formatInvoiceNumber(year, sequence);
    }

    @Override
    public void validateNextSequence(String invoiceNumber) {
        int year = Year.now().getValue();
        long lastSequence = extractSequence(invoiceNumber);

         generateNextInvoiceSequenceUseCase.storeNextSequence(year, lastSequence);
    }

    private String formatInvoiceNumber(int year, long sequence) {
        return "FAC-" + year + "-" + String.format("%05d", sequence);
    }

    private long extractSequence(String invoiceNumber) {
        String[] parts = invoiceNumber.split("-");
        return Long.parseLong(parts[2]);
    }
}
