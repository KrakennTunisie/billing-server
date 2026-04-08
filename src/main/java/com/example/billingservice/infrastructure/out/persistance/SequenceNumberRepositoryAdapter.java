package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.SequenceNumberCounterRepositoryPort;
import com.example.billingservice.domain.enums.SequenceNumberType;
import com.example.billingservice.domain.model.SequenceNumberCounter;
import com.example.billingservice.infrastructure.out.persistance.entity.SequenceNumberCounterEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.SequenceNumberCounterMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.SequenceNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SequenceNumberRepositoryAdapter implements SequenceNumberCounterRepositoryPort {
    private  final SequenceNumberRepository sequenceNumberRepository;
    private final SequenceNumberCounterMapper sequenceNumberCounterMapper;

    @Override
    public Optional<SequenceNumberCounter> findByYearAndDocumentForUpdate(SequenceNumberType sequenceNumberType, Integer year) {
        return sequenceNumberRepository.findByTypeAndYearForUpdate(sequenceNumberType,year)
                .map(sequenceNumberCounterMapper::toDomain);
    }

    @Override
    @Transactional
    public SequenceNumberCounter save(SequenceNumberCounter sequenceNumberCounter) {
        SequenceNumberCounterEntity entity = sequenceNumberCounterMapper.toEntity(sequenceNumberCounter);

        SequenceNumberCounterEntity savedEntity = sequenceNumberRepository.save(entity);

        return sequenceNumberCounterMapper.toDomain(savedEntity);
    }
}
