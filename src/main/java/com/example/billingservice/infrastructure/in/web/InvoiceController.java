package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.application.service.GenerateInvoiceNumberService;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.SequenceNumberType;
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


@Tag(name= "API Factures", description = "Gestion des factures")
@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceUseCase invoiceUseCase;
    private final GenerateInvoiceNumberService generateInvoiceNumberService;
    private final ObjectMapper objectMapper;

    private static final String CLIENT_INVOICES = "/client-invoices";
    private static final String SUPPLIER_INVOICES = "/supplier-invoices";

    @Operation(summary = "Numéro facture suivant", description = "Générer le numéro facture suivant")
    @GetMapping(path = "/next-number")
    public ResponseEntity <NextNumberDTO> genereateNextInvoiceNumber()
    {
        NextNumberDTO nextNumberDTO = NextNumberDTO.builder()
                .value(generateInvoiceNumberService.generate(SequenceNumberType.INVOICE))
                .build();
        return ResponseEntity.ok(nextNumberDTO);
    }
    @Operation(summary = "Créer une facture client", description = "Ajoute une nouvelle facture client")
    @PostMapping(path = CLIENT_INVOICES)
    public ResponseEntity<InvoiceDTO> createClientInvoice (@Valid @ModelAttribute InvoiceCreateDTO form,
                                                           @RequestParam(value = "invoiceItemsList", required = true) String invoiceItemsJson
                                                           )
            throws DataIntegrityViolationException, IOException {

        List<InvoiceItemCreateDTO> invoiceItems = objectMapper.readValue(
                invoiceItemsJson,
                new TypeReference<List<InvoiceItemCreateDTO>>() {}
        );

        System.out.println(invoiceItems);
        form.setInvoiceItems(invoiceItems);
        return ResponseEntity.status(201).body(invoiceUseCase.createClientInvoice(form));
    }

    @Operation(summary = "Liste des factures clients")
    @GetMapping(CLIENT_INVOICES)
    public ResponseEntity <Page<InvoicePageItemDTO>> getClientsInvoices(@RequestParam(required = false) String keyword,
                                                                        @RequestParam(required = false) String status,
                                                                        @RequestParam int page )
    {
        return ResponseEntity.ok(invoiceUseCase.getClientsInvoices(keyword, status, page));
    }

    @Operation(summary = "Mise à jour de facture client", description = "Mettre à jour une facture existante")
    @PatchMapping(path = CLIENT_INVOICES, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InvoiceDTO> updateClientInvoice (@Valid @ModelAttribute InvoiceUpdateDTO form,
                                                           @RequestParam(value = "invoiceItemsList", required = true) String invoiceItemsJson
    ) throws DataIntegrityViolationException, IOException {

        List<InvoiceItemCreateDTO> invoiceItems = objectMapper.readValue(
                invoiceItemsJson,
                new TypeReference<List<InvoiceItemCreateDTO>>() {}
        );

        System.out.println(invoiceItems);
        form.setInvoiceItems(invoiceItems);
        return ResponseEntity.status(201).body(invoiceUseCase.updateClientInvoice(form));
    }

    @GetMapping(CLIENT_INVOICES+"/{id}")
    @Operation(summary = "Détails d'une facture client")
    public ResponseEntity<InvoiceDTO> getClientInvoiceById(@Parameter(description = "ID du facture") @PathVariable String id)
    {
        InvoiceDTO invoiceDTO =  invoiceUseCase.getClientInvoiceById(UUID.fromString(id));
        return ResponseEntity.status(201).body(invoiceDTO);
    }

    @PatchMapping(CLIENT_INVOICES+"/{invoiceId}/status")
    public ResponseEntity<InvoiceDTO> updateClientInvoiceStatus(
            @PathVariable String invoiceId,
            @Valid @RequestBody UpdateInvoiceStatusRequest request
    ) {

        InvoiceDTO updated = invoiceUseCase.updateClientInvoiceStatus(
                UUID.fromString(invoiceId),
                InvoiceStatus.valueOf(request.getStatus())
        );

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(CLIENT_INVOICES+"/{id}")
    @Operation(summary = "Suppression d'une facture client")
    public ResponseEntity<Void> deleteClientInvoice(@Parameter(description = "ID du facture") @PathVariable String id)
    {
        invoiceUseCase.deleteClientInvoice(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    //SUPPLIER Invoices

    @Operation(summary = "Liste des factures fournisseurs")
    @GetMapping(SUPPLIER_INVOICES)
    public ResponseEntity <Page<InvoicePageItemDTO>> getSuppliersInvoices(@RequestParam(required = false) String keyword,
                                                                        @RequestParam(required = false) String filter,
                                                                        @RequestParam int page )
    {
        return ResponseEntity.ok(invoiceUseCase.getSuppliersInvoices(keyword, filter, page));
    }

    @Operation(summary = "Créer une facture fournisseur", description = "Ajoute une nouvelle facture fournisseur")
    @PostMapping(path = SUPPLIER_INVOICES, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InvoiceDTO> createSupplierInvoice (@Valid @ModelAttribute InvoiceCreateDTO form)
            throws DataIntegrityViolationException, IOException {
        return ResponseEntity.status(201).body(invoiceUseCase.createInvoice(form));
    }


    @GetMapping(SUPPLIER_INVOICES+"/{id}")
    @Operation(summary = "Détails d'une facture client")
    public ResponseEntity<InvoiceDTO> getSupplierInvoiceById(@Parameter(description = "ID du facture") @PathVariable String id)
    {
        InvoiceDTO invoiceDTO =  invoiceUseCase.getInvoiceById(UUID.fromString(id));
        return ResponseEntity.status(201).body(invoiceDTO);

    }

    @Operation(summary = "Mise à jour de facture client", description = "Mettre à jour une facture existante")
    @PatchMapping(path = SUPPLIER_INVOICES, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InvoiceDTO> updateSupplierInvoice (@Valid @ModelAttribute InvoiceUpdateDTO form)
            throws DataIntegrityViolationException, IOException {
        return ResponseEntity.status(201).body(invoiceUseCase.updateInvoice(form));
    }


    @PatchMapping(SUPPLIER_INVOICES+"/{invoiceId}/status")
    public ResponseEntity<InvoiceDTO> updateSupplierInvoiceStatus(
            @PathVariable String invoiceId,
            @Valid @RequestBody UpdateInvoiceStatusRequest request
    ) {

        InvoiceDTO updated = invoiceUseCase.updateInvoiceStatus(
                UUID.fromString(invoiceId),
                InvoiceStatus.valueOf(request.getStatus())
        );

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(SUPPLIER_INVOICES+"/{id}")
    @Operation(summary = "Suppression d'une facture fournisseur")
    public ResponseEntity<Void> deleteSupplierInvoice(@Parameter(description = "ID du facture") @PathVariable String id)
    {
        invoiceUseCase.deleteInvoice(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
