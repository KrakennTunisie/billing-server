package com.example.billingservice.application.ports.out;


import com.example.billingservice.domain.enums.SequenceNumberType;
import com.example.billingservice.domain.model.SequenceNumberCounter;

import java.util.Optional;

public interface SequenceNumberCounterRepositoryPort {

    Optional<SequenceNumberCounter> findByYearAndDocumentForUpdate(SequenceNumberType sequenceNumberType, Integer year);

    SequenceNumberCounter save(SequenceNumberCounter sequenceNumberCounter);
}
