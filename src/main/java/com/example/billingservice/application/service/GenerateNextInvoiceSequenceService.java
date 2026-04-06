package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.GenerateNextInvoiceSequenceUseCase;
import com.example.billingservice.application.ports.out.InvoiceCounterRepositoryPort;
import com.example.billingservice.domain.model.InvoiceCounter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GenerateNextInvoiceSequenceService implements GenerateNextInvoiceSequenceUseCase {
    private final InvoiceCounterRepositoryPort invoiceCounterRepositoryPort;

    public GenerateNextInvoiceSequenceService(InvoiceCounterRepositoryPort invoiceCounterRepositoryPort) {
        this.invoiceCounterRepositoryPort = invoiceCounterRepositoryPort;
    }

    @Override
    @Transactional
    public Long generateNextSequence(Integer year) {
        InvoiceCounter counter = invoiceCounterRepositoryPort.findByYearForUpdate(year)
                .orElse(
                        InvoiceCounter.builder()
                                .year(year)
                                .lastSequence(0L)
                                .build()
                );

        long nextSequence = counter.getLastSequence() + 1;

        return nextSequence;
    }

    @Override
    @Transactional
    public void storeNextSequence(Integer year, long nextSequence) {
        InvoiceCounter counter = invoiceCounterRepositoryPort.findByYearForUpdate(year)
                .orElse(
                        InvoiceCounter.builder()
                                .year(year)
                                .lastSequence(0L)
                                .build()
                );

        counter.setLastSequence(nextSequence);

        invoiceCounterRepositoryPort.save(counter);

    }
}
