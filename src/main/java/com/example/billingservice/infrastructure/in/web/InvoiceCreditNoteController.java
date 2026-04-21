package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.InvoiceCreditNoteUseCase;
import com.example.billingservice.application.service.GenerateInvoiceNumberService;
import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;
import com.example.billingservice.domain.enums.SequenceNumberType;
import com.example.billingservice.domain.model.InvoiceCreditNote;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.UUID;

@Tag(name= "API Factures d'Avoir", description = "Gestion des factures d'avoir")
@RestController
@RequestMapping("/api/credit-note-invoices")
@RequiredArgsConstructor
public class InvoiceCreditNoteController {

    private final InvoiceCreditNoteUseCase invoiceCreditNoteUseCase;
    private final GenerateInvoiceNumberService generateInvoiceNumberService;
    private final ObjectMapper objectMapper;

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
    public ResponseEntity<InvoiceCreditNoteDTO> createClientInvoice (
            @Valid @ModelAttribute InvoiceCreditNoteCreateDTO form,
            @RequestParam(value = "invoiceCreditNoteItemsList", required = true) String invoiceItemsJson
    )
            throws DataIntegrityViolationException, IOException {

        List<InvoiceCreditNoteItemCreateDTO> invoiceItems = objectMapper.readValue(
                invoiceItemsJson,
                new TypeReference<List<InvoiceCreditNoteItemCreateDTO>>() {}
        );

        invoiceItems.forEach(System.out::println);

        System.out.println(invoiceItems);
        form.setInvoiceItems(invoiceItems);
        return ResponseEntity.status(201).body(invoiceCreditNoteUseCase.create(form));
    }

    @Operation(summary = "Liste des factures d'avoir")
    @GetMapping("/invoice/{id}")
    public ResponseEntity <Page<InvoiceCreditNotePageItemDTO>> getClientsInvoices(@PathVariable String id,
                                                                        @RequestParam(required = false) String keyword,
                                                                        @RequestParam(required = false) String filter,
                                                                        @RequestParam int page )
    {
        return ResponseEntity.ok(
                invoiceCreditNoteUseCase.getInvoiceCreditNotes(UUID.fromString(id),keyword, filter, page)
        );
    }


    @GetMapping("/{invoiceCreditNoteNumber}")
    @Operation(summary = "get Invoice By invoiceCreditNoteNumber")
    public ResponseEntity<InvoiceCreditNoteDetailsDTO> getInvoiceByiD(@Parameter(description = "référence du facture") @PathVariable String invoiceCreditNoteNumber)
    {

        InvoiceCreditNoteDetailsDTO invoiceDTO =
                invoiceCreditNoteUseCase.getInvoiceCreditNoteByInvoiceCreditNoteNumber(invoiceCreditNoteNumber);
        return ResponseEntity.status(201).body(invoiceDTO);

    }

    @PatchMapping("/{invoiceCreditNoteNumber}/status")
    public ResponseEntity<InvoiceCreditNoteDetailsDTO> updateStatus(
            @PathVariable String invoiceCreditNoteNumber,
            @Valid @ModelAttribute UpdateInvoiceCreditNoteStatusRequest request
    ) {

        InvoiceCreditNoteDetailsDTO updated = invoiceCreditNoteUseCase.updateInvoiceCreditNoteStatus(
                invoiceCreditNoteNumber,
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
