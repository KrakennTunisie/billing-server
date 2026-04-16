package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.InvoiceCreditNoteUseCase;
import com.example.billingservice.application.service.GenerateInvoiceNumberService;
import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;
import com.example.billingservice.domain.enums.SequenceNumberType;
import com.example.billingservice.domain.model.InvoiceCreditNote;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@Tag(name= "API Factures d'Avoir", description = "Gestion des factures d'avoir")
@RestController
@RequestMapping("/api/credit-note-invoices")
@RequiredArgsConstructor
public class InvoiceCreditNoteController {

    private final InvoiceCreditNoteUseCase invoiceCreditNoteUseCase;
    private final GenerateInvoiceNumberService generateInvoiceNumberService;

    @Operation(summary = "Numéro facture suivant", description = "Générer le numéro facture suivant")
    @GetMapping(path = "/next-number")
    public ResponseEntity <NextNumberDTO> generateNextCreditNoteNumber()
    {
        NextNumberDTO nextNumberDTO = NextNumberDTO.builder()
                .value(generateInvoiceNumberService.generate(SequenceNumberType.CREDIT_NOTE))
                .build();
        return ResponseEntity.ok(nextNumberDTO);
    }
    @Operation(summary = "Créer une facture d'avoir", description = "Ajoute une nouvelle facture d'avoir")
    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InvoiceCreditNoteDTO> createClientInvoice (@Valid @ModelAttribute InvoiceCreditNoteCreateDTO form)
            throws DataIntegrityViolationException, IOException {
        return ResponseEntity.status(201).body(invoiceCreditNoteUseCase.create(form));
    }

    @Operation(summary = "Liste des factures d'avoir")
    @GetMapping("/invoice/{id}")
    public ResponseEntity <Page<InvoiceCreditNotePageItemDTO>> getClientsInvoices(@PathVariable String id,
                                                                        @RequestParam(required = false) String keyword,
                                                                        @RequestParam(required = false) String status,
                                                                        @RequestParam int page )
    {
        return ResponseEntity.ok(
                invoiceCreditNoteUseCase.getInvoiceCreditNotes(UUID.fromString(id),keyword, status, page)
        );
    }


    @GetMapping("/{id}")
    @Operation(summary = "get Invoice By id")
    public ResponseEntity<InvoiceCreditNote> getInvoiceByiD(@Parameter(description = "ID du facture") @PathVariable String id)
    {
        InvoiceCreditNote invoiceDTO =  invoiceCreditNoteUseCase.getInvoiceCreditNote(UUID.fromString(id));
        return ResponseEntity.status(201).body(invoiceDTO);

    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<InvoiceCreditNoteDTO> updateStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateInvoiceCreditNoteStatusRequest request
    ) {

        InvoiceCreditNoteDTO updated = invoiceCreditNoteUseCase.updateInvoiceCreditNoteStatus(
                UUID.fromString(id),
                InvoiceCreditNoteStatus.valueOf(request.getStatus())
        );

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Suppression d'une facture d'avoir")
    public ResponseEntity<Void> deleteInvoiceCreditNote(
            @Parameter(description = "ID du facture d'avoir") @PathVariable String id)
    {
        invoiceCreditNoteUseCase.deleteInvoiceCreditNote(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
