package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.PurchaseOrderUseCase;
import com.example.billingservice.domain.model.PurchaseOrder;
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

@Tag(name= "API Bon de Commande", description = "Gestion des bon de commandes")
@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderUseCase purchaseOrderUseCase;

    @Operation(summary = "Créer un bon de commande", description = "Ajoute un nouveau bon de commande")
    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder (@Valid @ModelAttribute PurchaseOrderCreateDTO form)
            throws DataIntegrityViolationException, IOException {
        return ResponseEntity.status(201).body(purchaseOrderUseCase.createPurchaseOrder(form));
    }

    @Operation(summary = "Liste des bons de commande")
    @GetMapping("/")
    public ResponseEntity <Page<PurchaseOrderPageItemDTO>> getPurchaseOrders(@RequestParam(required = false) String keyword,
                                                                        @RequestParam int page )
    {
        return ResponseEntity.ok(purchaseOrderUseCase.getPurchaseOrders(keyword, page));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Détails d'un bon de commande")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@Parameter(description = "ID du bon de commande") @PathVariable String id)
    {
        PurchaseOrder purchaseOrder =  purchaseOrderUseCase.getById(UUID.fromString(id));
        return ResponseEntity.status(201).body(purchaseOrder);

    }

    @Operation(summary = "Mise à jour de Bon de commande", description = "Mettre à jour un Bon de commande existant")
    @PatchMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PurchaseOrderDTO> updatePurchaseOrder (@Valid @ModelAttribute PurchaseOrderUpdateDTO form)
            throws DataIntegrityViolationException, IOException {
        return ResponseEntity.status(201).body(purchaseOrderUseCase.updatePurchaseOrder(form));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Suppression d'un bon de commande")
    public ResponseEntity<Void> deletePurchaseOrder(@Parameter(description = "ID du bon de commande") @PathVariable String id)
    {
        purchaseOrderUseCase.deletePurchaseOrder(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
