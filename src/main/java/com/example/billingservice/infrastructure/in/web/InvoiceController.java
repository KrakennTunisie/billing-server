package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.domain.enums.InvoiceStatus;
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


@Tag(name= "API Factures", description = "Gestion des factures")
@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceUseCase invoiceUseCase;

    @Operation(summary = "Créer une facture client", description = "Ajoute une nouvelle facture client")
    @PostMapping(path = "/client-invoices", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InvoiceDTO> createClientInvoice (@Valid @ModelAttribute InvoiceCreateDTO form)
            throws DataIntegrityViolationException, IOException {
        return ResponseEntity.status(201).body(invoiceUseCase.createInvoice(form));
    }

    @Operation(summary = "Liste des factures clients")
    @GetMapping("/client-invoices")
    public ResponseEntity <Page<InvoicePageItemDTO>> getClientsInvoices(@RequestParam(required = false) String keyword,
                                                                        @RequestParam(required = false) String status,
                                                                        @RequestParam int page )
    {
        return ResponseEntity.ok(invoiceUseCase.getClientsInvoices(keyword, status, page));
    }

    @Operation(summary = "Mise à jour de facture", description = "Mettre à jour une facture existante")
    @PatchMapping(path = "/client-invoices", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InvoiceDTO> updateInvoice (@Valid @ModelAttribute InvoiceUpdateDTO form)
            throws DataIntegrityViolationException, IOException {
        return ResponseEntity.status(201).body(invoiceUseCase.updateInvoice(form));
    }

    @GetMapping("/client-invoices/{id}")
    @Operation(summary = "Suppression d'un client")
    public ResponseEntity<InvoiceDTO> getInvoiceByiD(@Parameter(description = "ID du facture") @PathVariable String id)
    {
        InvoiceDTO invoiceDTO =  invoiceUseCase.getInvoiceById(UUID.fromString(id));
        return ResponseEntity.status(201).body(invoiceDTO);

    }

    @PatchMapping("/{invoiceId}/status")
    public ResponseEntity<InvoiceDTO> updateStatus(
            @PathVariable String invoiceId,
            @Valid @RequestBody UpdateInvoiceStatusRequest request
    ) {

        InvoiceDTO updated = invoiceUseCase.updateInvoiceStatus(
                UUID.fromString(invoiceId),
                InvoiceStatus.valueOf(request.getStatus())
        );

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/client-invoices/{id}")
    @Operation(summary = "Suppression d'un client")
    public ResponseEntity<Void> deleteFacture(@Parameter(description = "ID du facture") @PathVariable String id)
    {
        invoiceUseCase.deleteInvoice(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
