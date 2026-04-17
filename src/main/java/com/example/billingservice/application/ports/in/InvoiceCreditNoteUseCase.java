package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;
import com.example.billingservice.domain.model.InvoiceCreditNote;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.UUID;

public interface InvoiceCreditNoteUseCase {


    Page<InvoiceCreditNotePageItemDTO> getInvoiceCreditNotes(UUID idInvoice, String keyword , String status , int page);

    InvoiceCreditNote getInvoiceCreditNote(UUID idInvoiceCreditNote);

    InvoiceCreditNoteDetailsDTO getInvoiceCreditNoteByInvoiceCreditNoteNumber(String creditNoteNumber);

    InvoiceCreditNoteDTO create(InvoiceCreditNoteCreateDTO createDTO) throws IOException;

    InvoiceCreditNoteDTO updateInvoiceCreditNoteStatus(
            UUID idInvoiceCreditNote, InvoiceCreditNoteStatus invoiceCreditNoteStatus);


    void deleteInvoiceCreditNote(UUID invoiceCreditNoteId);

    boolean existsByInvoiceCreditNoteNumber(String invoiceCreditNoteNumber);

    boolean existsByInvoiceCreditNoteId(UUID invoiceCreditNoteId);

    boolean existsInvoiceCreditNoteEntityByInvoice(UUID idInvoice);

}
