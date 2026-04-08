package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.model.SequenceNumberCounter;
import com.example.billingservice.infrastructure.out.persistance.entity.SequenceNumberCounterEntity;
import org.springframework.stereotype.Component;

@Component
public class SequenceNumberCounterMapper {

    public SequenceNumberCounter toDomain(SequenceNumberCounterEntity entity) {
        if (entity == null) {
            return null;
        }

        return SequenceNumberCounter.builder()
                .id(entity.getId())
                .year(entity.getYear())
                .lastSequence(entity.getLastSequence())
                .sequenceNumberType(entity.getSequenceNumberType())
                .build();
    }

    public SequenceNumberCounterEntity toEntity(SequenceNumberCounter domain) {
        if (domain == null) {
            return null;
        }

        SequenceNumberCounterEntity entity = new SequenceNumberCounterEntity();
        entity.setId(domain.getId());
        entity.setYear(domain.getYear());
        entity.setSequenceNumberType(domain.getSequenceNumberType());
        entity.setLastSequence(domain.getLastSequence());
        return entity;
    }

}
