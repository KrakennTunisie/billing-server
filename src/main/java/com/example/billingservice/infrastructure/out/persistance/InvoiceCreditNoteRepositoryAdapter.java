package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.InvoiceCreditNoteRepositoryPort;
import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.InvoiceCreditNote;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceCreditNotePageItemDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCreditNoteEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.InvoiceCreditNoteMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.InvoiceCreditNoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InvoiceCreditNoteRepositoryAdapter implements InvoiceCreditNoteRepositoryPort {

    private InvoiceCreditNoteRepository invoiceCreditNoteRepository;
    private InvoiceCreditNoteMapper invoiceCreditNoteMapper;


    @Override
    public Page<InvoiceCreditNotePageItemDTO> getInvoiceCreditNotes(
            UUID idInvoice, String keyword, InvoiceCreditNoteStatus status, int page) {
        try {

            PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("issueDate").descending());
            Page<InvoiceCreditNoteEntity> entities = invoiceCreditNoteRepository
                    .getCreditNotesByInvoiceId(idInvoice, keyword, status, pageRequest);

            List<InvoiceCreditNotePageItemDTO> invoices = entities.getContent()
                    .stream()
                    .map(invoiceCreditNoteMapper::toDomain)
                    .map(invoiceCreditNoteMapper::toPageItemDTO)
                    .collect(Collectors.toList());

            return new PageImpl<>(invoices, pageRequest, entities.getTotalElements());

        } catch (DataAccessException ex) {
            throw BillingException.internalError("Erreur de fetch des factures d'avoir: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public InvoiceCreditNote create(InvoiceCreditNote createDTO) {
        InvoiceCreditNoteEntity entity = invoiceCreditNoteMapper.toEntity(createDTO);
        InvoiceCreditNoteEntity savedEntity = invoiceCreditNoteRepository.save(entity);
        InvoiceCreditNote invoice1 = invoiceCreditNoteMapper.toDomain(savedEntity);/*
        entity.getInvoiceEvents().forEach(
                invoiceEventEntity -> invoiceEventEntity.setInvoice(savedEntity)
        );
        jpaInvoiceEventRepository.saveAll(entity.getInvoiceEvents());*/


        return  invoice1;
    }

    @Override
    public InvoiceCreditNote getById(UUID idInvoiceCreditNote) {
        InvoiceCreditNoteEntity invoiceCreditNoteEntity = invoiceCreditNoteRepository.getReferenceById(idInvoiceCreditNote);
        return invoiceCreditNoteMapper.toDomain(invoiceCreditNoteEntity);
    }

    @Override
    public InvoiceCreditNote updateStatus(InvoiceCreditNote invoiceCreditNote, InvoiceCreditNoteStatus newStatus) {
        InvoiceCreditNoteEntity entity = invoiceCreditNoteMapper.toEntity(invoiceCreditNote);

        entity.setInvoiceCreditNoteStatus(newStatus);

        return invoiceCreditNoteMapper.toDomain(invoiceCreditNoteRepository.save(entity));
    }

    @Override
    public boolean existsByInvoiceCreditNoteNumber(String invoiceNumber) {
        return invoiceCreditNoteRepository.existsByInvoiceCreditNoteNumber(invoiceNumber);
    }

    @Override
    public boolean existsByInvoiceCreditNoteId(UUID invoiceId) {
        return invoiceCreditNoteRepository.existsById(invoiceId);
    }

    @Override
    public void delete(UUID invoiceCreditNoteId) {
        InvoiceCreditNote invoiceCreditNote = getById(invoiceCreditNoteId);
        InvoiceCreditNoteEntity invoiceCreditNoteEntity = invoiceCreditNoteMapper.toEntity(invoiceCreditNote);
        if(invoiceCreditNote.getInvoiceCreditNoteStatus()==InvoiceCreditNoteStatus.DRAFT){
            invoiceCreditNoteRepository.delete(invoiceCreditNoteEntity);
        }
        else if(invoiceCreditNote.getInvoiceCreditNoteStatus()==InvoiceCreditNoteStatus.PENDING){
            invoiceCreditNoteEntity.setInvoiceCreditNoteStatus(InvoiceCreditNoteStatus.CANCELLED);
            invoiceCreditNoteRepository.save(invoiceCreditNoteEntity);
        }
        else {
            throw BillingException.badRequest("Impossible de supprimer une facture d'avoir dèjà traitée");
        }

    }
}
