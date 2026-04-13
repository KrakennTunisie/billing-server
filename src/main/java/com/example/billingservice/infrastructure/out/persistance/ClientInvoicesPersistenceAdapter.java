package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.ClientInvoicesRepositoryPort;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.InvoiceType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoicePageItemDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.ClientInvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.InvoiceMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.ClientInvoicesRepository;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ClientInvoicesPersistenceAdapter implements ClientInvoicesRepositoryPort {

    private final ClientInvoicesRepository clientInvoicesRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    public Page<InvoicePageItemDTO> findAllInvoices(String keyword, InvoiceStatus status, int page, InvoiceType type) {
        try {

            PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("issueDate").descending());
            Page<InvoiceEntity> entities = clientInvoicesRepository.getInvoices(keyword, status, pageRequest);

            List<InvoicePageItemDTO> invoices = entities.getContent()
                    .stream()
                    .map(invoiceEntity -> invoiceMapper.toDomain(invoiceEntity, InvoiceType.SALE))
                    .map(invoiceMapper::toInvoicePageItemDTO)
                    .collect(Collectors.toList());

            return new PageImpl<>(invoices, pageRequest, entities.getTotalElements());

        } catch (DataAccessException ex) {
            throw BillingException.internalError("Erreur de fetch des factures: " + ex.getMessage());
        }    }

    @Override
    public InvoiceDTO save(Invoice invoice) {
        ClientInvoiceEntity entity = (ClientInvoiceEntity) invoiceMapper.toEntity(invoice);
        ClientInvoiceEntity savedEntity = clientInvoicesRepository.save(entity);
        Invoice invoice1 = invoiceMapper.toDomain(savedEntity, invoice.getInvoiceType());/*
        entity.getInvoiceEvents().forEach(
                invoiceEventEntity -> invoiceEventEntity.setInvoice(savedEntity)
        );
        jpaInvoiceEventRepository.saveAll(entity.getInvoiceEvents());*/


        return  invoiceMapper.toDTO(invoice1);
    }

    @Override
    public InvoiceDTO update(Invoice invoice) {
        ClientInvoiceEntity entity = (ClientInvoiceEntity) invoiceMapper.toEntity(invoice);
        entity.setIdInvoice(invoice.getIdInvoice());
        InvoiceEntity savedEntity = clientInvoicesRepository.save(entity);
        Invoice invoice1 = invoiceMapper.toDomain(savedEntity, invoice.getInvoiceType());/*
        entity.getInvoiceEvents().forEach(
                invoiceEventEntity -> invoiceEventEntity.setInvoice(savedEntity)
        );
        jpaInvoiceEventRepository.saveAll(entity.getInvoiceEvents());*/

        return  invoiceMapper.toDTO(invoice1);
    }

    @Override
    public InvoiceDTO updateStatus(UUID invoiceId, InvoiceStatus newStatus) {
        ClientInvoiceEntity entity = clientInvoicesRepository.getClientInvoiceEntityByIdInvoice(invoiceId);
        entity.setInvoiceStatus(newStatus);
        Invoice invoice1 = invoiceMapper.toDomain(clientInvoicesRepository.save(entity), InvoiceType.SALE);

        return  invoiceMapper.toDTO(invoice1);
    }

    @Override
    public InvoiceDTO getById(UUID idInvoice) {
        ClientInvoiceEntity entity = clientInvoicesRepository.getClientInvoiceEntityByIdInvoice(idInvoice);
        Invoice invoice = invoiceMapper.toDomain(entity, InvoiceType.SALE);

        return invoiceMapper.toDTO(invoice);    }

    @Override
    public Invoice getInvoice(UUID idInvoice) {
        ClientInvoiceEntity entity = clientInvoicesRepository.getClientInvoiceEntityByIdInvoice(idInvoice);
        return invoiceMapper.toDomain(entity, InvoiceType.SALE);
    }

    @Override
    public void delete(UUID idInvoice) {
        ClientInvoiceEntity entity = clientInvoicesRepository.getClientInvoiceEntityByIdInvoice(idInvoice);
        if(entity.getInvoiceStatus()!=InvoiceStatus.DRAFT){
            entity.setInvoiceStatus(InvoiceStatus.CANCELLED);
        }
        else {
            clientInvoicesRepository.delete(entity);
        }
    }

    @Override
    public boolean existsByInvoiceNumber(String invoiceNumber) {
        return clientInvoicesRepository.existsByReference(invoiceNumber);
    }

    @Override
    public boolean existsByInvoiceId(UUID invoiceId) {
        return clientInvoicesRepository.existsByIdInvoice(invoiceId);
    }
}
