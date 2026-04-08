package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;
import com.example.billingservice.domain.model.InvoiceCreditNote;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceCreditNotePageItemDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface InvoiceCreditNoteRepositoryPort {

    Page<InvoiceCreditNotePageItemDTO> getInvoiceCreditNotes(
            UUID idInvoice, String keyword , InvoiceCreditNoteStatus status , int page);

    InvoiceCreditNote create(InvoiceCreditNote invoiceCreditNote);


    InvoiceCreditNote getById(UUID idInvoiceCreditNote);

    InvoiceCreditNote updateStatus(InvoiceCreditNote invoiceCreditNote, InvoiceCreditNoteStatus newStatus);

    boolean existsByInvoiceCreditNoteNumber(String invoiceNumber);

    boolean existsByInvoiceCreditNoteId(UUID invoiceId);


    void delete(UUID idInvoiceCreditNote);


}
