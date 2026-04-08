package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;
import com.example.billingservice.domain.model.InvoiceCreditNote;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceCreditNoteCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceCreditNoteDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceCreditNotePageItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceDTO;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.UUID;

public interface InvoiceCreditNoteUseCase {


    Page<InvoiceCreditNotePageItemDTO> getInvoiceCreditNotes(UUID idInvoice, String keyword , String status , int page);

    InvoiceCreditNote getInvoiceCreditNote(UUID idInvoiceCreditNote);

    InvoiceCreditNoteDTO create(InvoiceCreditNoteCreateDTO createDTO) throws IOException;

    InvoiceCreditNoteDTO updateInvoiceCreditNoteStatus(
            UUID idInvoiceCreditNote, InvoiceCreditNoteStatus invoiceCreditNoteStatus);


    void deleteInvoiceCreditNote(UUID invoiceCreditNoteId);

    boolean existsByInvoiceCreditNoteNumber(String invoiceCreditNoteNumber);

    boolean existsByInvoiceCreditNoteId(UUID invoiceCreditNoteId);
}
