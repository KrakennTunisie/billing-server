package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.GenerateNextInvoiceSequenceUseCase;
import com.example.billingservice.application.ports.out.SequenceNumberCounterRepositoryPort;
import com.example.billingservice.domain.enums.SequenceNumberType;
import com.example.billingservice.domain.model.SequenceNumberCounter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class GenerateNextInvoiceSequenceService implements GenerateNextInvoiceSequenceUseCase {

    private final SequenceNumberCounterRepositoryPort sequenceNumberCounterRepositoryPort;


    @Override
    @Transactional
    public Long generateNextSequence(SequenceNumberType sequenceNumberType, Integer year) {
        SequenceNumberCounter counter = sequenceNumberCounterRepositoryPort.findByYearAndDocumentForUpdate(sequenceNumberType,year)
                .orElse(
                        SequenceNumberCounter.builder()
                                .year(year)
                                .sequenceNumberType(sequenceNumberType)
                                .lastSequence(0L)
                                .build()
                );

        long nextSequence = counter.getLastSequence()+1;

        return nextSequence;
    }

    @Override
    @Transactional
    public void storeNextSequence(SequenceNumberType type,
                                  Integer year,
                                  long nextSequence) {

        SequenceNumberCounter counter = sequenceNumberCounterRepositoryPort
                .findByYearAndDocumentForUpdate(type, year)
                .orElseGet(() -> SequenceNumberCounter.builder()
                        .year(year)
                        .sequenceNumberType(type)
                        .lastSequence(0L)
                        .build()
                );
        counter.setLastSequence(nextSequence);

        sequenceNumberCounterRepositoryPort.save(counter);
    }
}
