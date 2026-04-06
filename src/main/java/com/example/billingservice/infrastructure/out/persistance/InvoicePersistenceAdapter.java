package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.InvoiceRepositoryPort;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.InvoiceType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoicePageItemDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.InvoiceMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.JpaInvoiceEventRepository;
import com.example.billingservice.infrastructure.out.persistance.repository.JpaInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InvoicePersistenceAdapter implements InvoiceRepositoryPort {

    private final JpaInvoiceRepository jpaInvoiceRepository;
    private final JpaInvoiceEventRepository jpaInvoiceEventRepository;
    private final InvoiceMapper invoiceMapper;


    @Override
    public InvoiceDTO save(Invoice invoice) {
        InvoiceEntity entity = invoiceMapper.toEntity(invoice);
        InvoiceEntity savedEntity = jpaInvoiceRepository.save(entity);
        Invoice invoice1 = invoiceMapper.toDomain(savedEntity);/*
        entity.getInvoiceEvents().forEach(
                invoiceEventEntity -> invoiceEventEntity.setInvoice(savedEntity)
        );
        jpaInvoiceEventRepository.saveAll(entity.getInvoiceEvents());*/


        return  invoiceMapper.toDTO(invoice1);
    }

    @Override
    public InvoiceDTO update(Invoice invoice) {

        InvoiceEntity entity = invoiceMapper.toEntity(invoice);
        entity.setIdInvoice(invoice.getIdInvoice());
        InvoiceEntity savedEntity = jpaInvoiceRepository.save(entity);
        Invoice invoice1 = invoiceMapper.toDomain(savedEntity);/*
        entity.getInvoiceEvents().forEach(
                invoiceEventEntity -> invoiceEventEntity.setInvoice(savedEntity)
        );
        jpaInvoiceEventRepository.saveAll(entity.getInvoiceEvents());*/

        return  invoiceMapper.toDTO(invoice1);
    }

    @Override
    public InvoiceDTO updateStatus(UUID invoiceId, InvoiceStatus newStatus) {

        InvoiceEntity entity = jpaInvoiceRepository.getReferenceById(invoiceId);
        entity.setInvoiceStatus(newStatus);
        Invoice invoice1 = invoiceMapper.toDomain(jpaInvoiceRepository.save(entity));

        return  invoiceMapper.toDTO(invoice1);
    }

    @Override
    public InvoiceDTO getById(UUID idInvoice) {
        InvoiceEntity entity = jpaInvoiceRepository.getReferenceById(idInvoice);
        Invoice invoice = invoiceMapper.toDomain(entity);

        return invoiceMapper.toDTO(invoice);
    }

    @Override
    public void delete(UUID idInvoice) {
        InvoiceEntity entity = jpaInvoiceRepository.getReferenceById(idInvoice);
        if(entity.getInvoiceStatus()!=InvoiceStatus.DRAFT){
            entity.setInvoiceStatus(InvoiceStatus.CANCELLED);
        }
        else {
            jpaInvoiceRepository.delete(entity);
        }
    }

    @Override
    public boolean existsByInvoiceNumber(String invoiceNumber) {
        return jpaInvoiceRepository.existsByInvoiceNumber(invoiceNumber);
    }

    @Override
    public boolean existsByInvoiceId(UUID invoiceId) {
        return jpaInvoiceRepository.existsByIdInvoice(invoiceId);
    }

    @Override
    public Page<InvoicePageItemDTO> findAllInvoices(String keyword, InvoiceStatus status, int page, InvoiceType type) {
        try {

            PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("issueDate").descending());
            Page<InvoiceEntity> entities = jpaInvoiceRepository.getInvoices(keyword, status, type, pageRequest);

            List<InvoicePageItemDTO> invoices = entities.getContent()
                    .stream()
                    .map(invoiceMapper::toDomain)
                    .map(invoiceMapper::toInvoicePageItemDTO)
                    .collect(Collectors.toList());

            return new PageImpl<>(invoices, pageRequest, entities.getTotalElements());

        } catch (DataAccessException ex) {
            throw BillingException.internalError("Erreur de fetch des factures: " + ex.getMessage());
        }
    }
}
