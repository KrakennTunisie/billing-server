package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.InvoiceCounterRepositoryPort;
import com.example.billingservice.domain.model.InvoiceCounter;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCounterEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.InvoiceCounterMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.InvoiceCounterRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InvoiceCounterRepositoryAdapter implements InvoiceCounterRepositoryPort {
    private final InvoiceCounterRepository invoiceCounterJpaRepository;
    private final InvoiceCounterMapper invoiceCounterMapper;

    public InvoiceCounterRepositoryAdapter(InvoiceCounterRepository invoiceCounterJpaRepository,
                                           InvoiceCounterMapper invoiceCounterMapper) {
        this.invoiceCounterJpaRepository = invoiceCounterJpaRepository;
        this.invoiceCounterMapper = invoiceCounterMapper;
    }

    @Override
    public Optional<InvoiceCounter> findByYearForUpdate(Integer year) {
        return invoiceCounterJpaRepository.findByYearForUpdate(year)
                .map(invoiceCounterMapper::toDomain);
    }

    @Override
    public InvoiceCounter save(InvoiceCounter invoiceCounter) {
        InvoiceCounterEntity entity = invoiceCounterJpaRepository.findById(invoiceCounter.getYear())
                .orElseGet(() -> invoiceCounterMapper.toEntity(invoiceCounter));

        invoiceCounterMapper.updateEntity(invoiceCounter, entity);

        InvoiceCounterEntity savedEntity = invoiceCounterJpaRepository.save(entity);
        return invoiceCounterMapper.toDomain(savedEntity);
    }
}
