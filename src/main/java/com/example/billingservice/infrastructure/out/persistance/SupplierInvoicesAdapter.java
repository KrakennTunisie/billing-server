package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.SupplierInvoicesRepositoryPort;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.InvoiceType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoicePageItemDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.SupplierInvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.InvoiceMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.JpaInvoiceEventRepository;
import com.example.billingservice.infrastructure.out.persistance.repository.SupplierInvoicesRepository;
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
public class SupplierInvoicesAdapter implements SupplierInvoicesRepositoryPort {

    private final SupplierInvoicesRepository supplierInvoicesRepository;
    private final InvoiceMapper invoiceMapper;
    @Override
    public Page<InvoicePageItemDTO> findAllInvoices(String keyword, InvoiceStatus status, int page, InvoiceType type) {
        try {

            PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("issueDate").descending());
            Page<InvoiceEntity> entities = supplierInvoicesRepository.getInvoices(keyword, status, pageRequest);

            List<InvoicePageItemDTO> invoices = entities.getContent()
                    .stream()
                    .map(invoiceEntity -> invoiceMapper.toDomain(invoiceEntity, InvoiceType.PURCHASE))
                    .map(invoiceMapper::toInvoicePageItemDTO)
                    .collect(Collectors.toList());

            return new PageImpl<>(invoices, pageRequest, entities.getTotalElements());

        } catch (DataAccessException ex) {
            throw BillingException.internalError("Erreur de fetch des factures: " + ex.getMessage());
        }    }

    @Override
    public InvoiceDTO save(Invoice invoice) {
        SupplierInvoiceEntity entity = (SupplierInvoiceEntity) invoiceMapper.toEntity(invoice);
        SupplierInvoiceEntity savedEntity = supplierInvoicesRepository.save(entity);
        Invoice invoice1 = invoiceMapper.toDomain(savedEntity, invoice.getInvoiceType());/*
        entity.getInvoiceEvents().forEach(
                invoiceEventEntity -> invoiceEventEntity.setInvoice(savedEntity)
        );
        jpaInvoiceEventRepository.saveAll(entity.getInvoiceEvents());*/


        return  invoiceMapper.toDTO(invoice1);
    }

    @Override
    public InvoiceDTO update(Invoice invoice) {
        SupplierInvoiceEntity entity = (SupplierInvoiceEntity) invoiceMapper.toEntity(invoice);
        entity.setIdInvoice(invoice.getIdInvoice());
        InvoiceEntity savedEntity = supplierInvoicesRepository.save(entity);
        Invoice invoice1 = invoiceMapper.toDomain(savedEntity, invoice.getInvoiceType());/*
        entity.getInvoiceEvents().forEach(
                invoiceEventEntity -> invoiceEventEntity.setInvoice(savedEntity)
        );
        jpaInvoiceEventRepository.saveAll(entity.getInvoiceEvents());*/

        return  invoiceMapper.toDTO(invoice1);
    }

    @Override
    public InvoiceDTO updateStatus(UUID invoiceId, InvoiceStatus newStatus) {
        SupplierInvoiceEntity entity = supplierInvoicesRepository.getSupplierInvoiceEntityByIdInvoice(invoiceId);
        entity.setInvoiceStatus(newStatus);
        Invoice invoice1 = invoiceMapper.toDomain(supplierInvoicesRepository.save(entity), InvoiceType.PURCHASE);

        return  invoiceMapper.toDTO(invoice1);
    }

    @Override
    public InvoiceDTO getById(UUID idInvoice) {
        SupplierInvoiceEntity entity = supplierInvoicesRepository.getSupplierInvoiceEntityByIdInvoice(idInvoice);
        Invoice invoice = invoiceMapper.toDomain(entity, InvoiceType.PURCHASE);

        return invoiceMapper.toDTO(invoice);    }

    @Override
    public Invoice getInvoice(UUID idInvoice) {
        SupplierInvoiceEntity entity = supplierInvoicesRepository.getSupplierInvoiceEntityByIdInvoice(idInvoice);
        return invoiceMapper.toDomain(entity, InvoiceType.PURCHASE);
    }

    @Override
    public void delete(UUID idInvoice) {
        SupplierInvoiceEntity entity = supplierInvoicesRepository.getSupplierInvoiceEntityByIdInvoice(idInvoice);
        if(entity.getInvoiceStatus()!=InvoiceStatus.DRAFT){
            entity.setInvoiceStatus(InvoiceStatus.CANCELLED);
        }
        else {
            supplierInvoicesRepository.delete(entity);
        }
    }

    @Override
    public boolean existsByInvoiceNumber(String invoiceNumber) {
        return supplierInvoicesRepository.existsByReference(invoiceNumber);
    }

    @Override
    public boolean existsByInvoiceId(UUID invoiceId) {
        return supplierInvoicesRepository.existsByIdInvoice(invoiceId);
    }
}
