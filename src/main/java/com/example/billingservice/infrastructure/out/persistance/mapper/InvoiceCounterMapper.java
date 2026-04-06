package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.model.InvoiceCounter;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCounterEntity;
import org.springframework.stereotype.Component;

@Component
public class InvoiceCounterMapper {

    public InvoiceCounter toDomain(InvoiceCounterEntity entity) {
        if (entity == null) {
            return null;
        }

        return InvoiceCounter.builder()
                .year(entity.getYear())
                .lastSequence(entity.getLastSequence())
                .build();
    }

    public InvoiceCounterEntity toEntity(InvoiceCounter domain) {
        if (domain == null) {
            return null;
        }

        InvoiceCounterEntity entity = new InvoiceCounterEntity();
        entity.setYear(domain.getYear());
        entity.setLastSequence(domain.getLastSequence());
        return entity;
    }

    public void updateEntity(InvoiceCounter domain, InvoiceCounterEntity entity) {
        if (domain == null || entity == null) {
            return;
        }

        entity.setYear(domain.getYear());
        entity.setLastSequence(domain.getLastSequence());
    }
}
