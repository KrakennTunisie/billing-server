package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.in.CreditNoteSynchronizationUseCase;
import com.example.billingservice.application.ports.out.ClientInvoicesRepositoryPort;
import com.example.billingservice.application.ports.out.InvoiceCreditNoteRepositoryPort;
import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.InvoiceCreditNote;
import com.example.billingservice.domain.model.InvoiceCreditNoteItem;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceCreditNoteDetailsDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceCreditNotePageItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceSummaryDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.ClientInvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCreditNoteEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.SupplierInvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.InvoiceCreditNoteMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.ClientInvoicesRepository;
import com.example.billingservice.infrastructure.out.persistance.repository.InvoiceCreditNoteRepository;
import com.example.billingservice.infrastructure.out.persistance.repository.SupplierInvoicesRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InvoiceCreditNoteRepositoryAdapter implements InvoiceCreditNoteRepositoryPort {

    private final InvoiceCreditNoteRepository invoiceCreditNoteRepository;
    private final InvoiceCreditNoteMapper invoiceCreditNoteMapper;
    private final CreditNoteSynchronizationUseCase creditNoteSynchronizationUseCase;
    private final ClientInvoicesRepository clientInvoicesRepository;
    private final SupplierInvoicesRepository supplierInvoicesRepository;


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
    //@Transactional
    public InvoiceCreditNote create(InvoiceCreditNote createDTO) {
        InvoiceCreditNoteEntity entity = invoiceCreditNoteMapper.toEntity(createDTO);
        InvoiceCreditNoteEntity savedEntity = invoiceCreditNoteRepository.save(entity);

        return invoiceCreditNoteMapper.toDomain(savedEntity);
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
            System.out.println("executing delete");
            synchronizeInvoiceItems(invoiceCreditNote);
            invoiceCreditNoteRepository.delete(invoiceCreditNoteEntity);
        }
        else if(invoiceCreditNote.getInvoiceCreditNoteStatus()==InvoiceCreditNoteStatus.IN_PROGRESS){
            invoiceCreditNoteEntity.setInvoiceCreditNoteStatus(InvoiceCreditNoteStatus.CANCELLED);
            System.out.println("executing update status");
            synchronizeInvoiceItems(invoiceCreditNote);

            invoiceCreditNoteRepository.save(invoiceCreditNoteEntity);

        }
        else {
            throw BillingException.badRequest("Impossible de supprimer une facture d'avoir dèjà traitée");
        }

    }

    @Override
    public InvoiceCreditNote getByInvoiceCreditNoteNumber(String invoiceCreditNoteNumber) {
        InvoiceCreditNoteEntity invoiceCreditNoteEntity =
                invoiceCreditNoteRepository
                        .getInvoiceCreditNoteEntityByInvoiceCreditNoteNumber(invoiceCreditNoteNumber);

        return invoiceCreditNoteMapper.toDomain(invoiceCreditNoteEntity);
    }

    @Override
    public boolean existsInvoiceCreditNoteEntityByInvoice(UUID idInvoice) {
        return invoiceCreditNoteRepository.existsInvoiceCreditNoteEntityByInvoice_IdInvoice(idInvoice);
    }

    @Override
    public List<InvoiceCreditNotePageItemDTO> getCreditNoteByClient(String idClient) {
        List<InvoiceCreditNoteEntity> clientsCreditNotes = new ArrayList<>();

        List<ClientInvoiceEntity> clientsInvoices = clientInvoicesRepository.getClientInvoices(UUID.fromString(idClient),PageRequest.of(0, 3));
        System.out.println(clientsInvoices.stream().collect(Collectors.toList()));
        clientsInvoices.forEach(invoiceEntity -> {
            List<InvoiceCreditNoteEntity> creditNotes = invoiceCreditNoteRepository
                    .getInvoiceCreditNoteEntityByInvoice_IdInvoice(invoiceEntity.getIdInvoice());
            clientsCreditNotes.addAll(creditNotes);
        });
        List<InvoiceCreditNote>  invoiceCreditNotes= clientsCreditNotes.stream()
                .map(creditNoteEntity -> invoiceCreditNoteMapper.toDomain(creditNoteEntity))
                .collect(Collectors.toList());
        return invoiceCreditNotes.stream()
                .map(creditNoteEntity -> invoiceCreditNoteMapper.toPageItemDTO(creditNoteEntity))
                .collect(Collectors.toList());
    }
    @Override
    public List<InvoiceCreditNotePageItemDTO> getCreditNoteBySupplier(String idSupplier) {
        List<InvoiceCreditNoteEntity> suppliersCreditNotes = new ArrayList<>();

        List<SupplierInvoiceEntity> suppliersInvoices = supplierInvoicesRepository.getAllSupplierInvoices(UUID.fromString(idSupplier),PageRequest.of(0, 3));

        suppliersInvoices.forEach(invoiceEntity -> {
            List<InvoiceCreditNoteEntity> creditNotes = invoiceCreditNoteRepository
                    .getInvoiceCreditNoteEntityByInvoice_IdInvoice(invoiceEntity.getIdInvoice());
            suppliersCreditNotes.addAll(creditNotes);
        });
     List<InvoiceCreditNote>  invoiceCreditNotes= suppliersCreditNotes.stream()
                .map(creditNoteEntity -> invoiceCreditNoteMapper.toDomain(creditNoteEntity))
                .collect(Collectors.toList());

        return invoiceCreditNotes.stream()
                .map(creditNoteEntity -> invoiceCreditNoteMapper.toPageItemDTO(creditNoteEntity))
                .collect(Collectors.toList());
    }


    private void synchronizeInvoiceItems(InvoiceCreditNote invoiceCreditNote){
        for (InvoiceCreditNoteItem invoiceCreditNoteItem : invoiceCreditNote.getInvoiceCreditNoteItems()){
            creditNoteSynchronizationUseCase.synchronize(invoiceCreditNoteItem.getInvoiceItem().getIdInvoiceItem(), -invoiceCreditNoteItem.getQuantity());
        }

    }
}
