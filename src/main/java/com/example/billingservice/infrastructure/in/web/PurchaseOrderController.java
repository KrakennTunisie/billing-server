package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.PurchaseOrderUseCase;
import com.example.billingservice.application.service.GenerateInvoiceNumberService;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.PurchaseOrderStatus;
import com.example.billingservice.domain.enums.SequenceNumberType;
import com.example.billingservice.domain.model.PurchaseOrder;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name= "API Bon de Commande", description = "Gestion des bon de commandes")
@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderUseCase purchaseOrderUseCase;
    private final GenerateInvoiceNumberService generateInvoiceNumberService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "Numéro facture suivant", description = "Générer le numéro facture suivant")
    @GetMapping(path = "/next-number")
    public ResponseEntity <NextNumberDTO> generatePurchaseOrderNumber()
    {
        NextNumberDTO nextNumberDTO = NextNumberDTO.builder()
                .value(generateInvoiceNumberService.generate(SequenceNumberType.PURCHASE_ORDER))
                .build();
        return ResponseEntity.ok(nextNumberDTO);
    }
    @Operation(summary = "Créer un bon de commande", description = "Ajoute un nouveau bon de commande")
    @PostMapping(path = "/client", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder (@Valid @ModelAttribute PurchaseOrderCreateDTO form,
      @RequestParam(value = "purchaseOrderItemsList", required = true) String purchaseOrderItemsJson)
            throws DataIntegrityViolationException, IOException {
        List<PurchaseOrderItemCreateDTO> purchaseOrderItems = objectMapper.readValue(
                purchaseOrderItemsJson,
                new TypeReference<List<PurchaseOrderItemCreateDTO>>() {}
        );
        System.out.println(purchaseOrderItems);
        form.setPurchaseOrderItems(purchaseOrderItems);
        return ResponseEntity.status(201).body(purchaseOrderUseCase.createClientPurchaseOrder(form));
    }

    @Operation(summary = "Liste des bons de commande")
    @GetMapping("/")
    public ResponseEntity <Page<PurchaseOrderPageItemDTO>> getPurchaseOrders(@RequestParam(required = false) String keyword, @RequestParam(required = false) String filter,
                                                                             @RequestParam int page )
    {
        return ResponseEntity.ok(purchaseOrderUseCase.getClientPurchaseOrders(keyword,filter, page));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Détails d'un bon de commande")
    public ResponseEntity<PurchaseOrderDTO> getPurchaseOrderById(@Parameter(description = "ID du bon de commande") @PathVariable String id)
    {
        PurchaseOrderDTO purchaseOrder =  purchaseOrderUseCase.getClientPurchaseOrderById(UUID.fromString(id));
        return ResponseEntity.status(201).body(purchaseOrder);

    }

    @GetMapping("/summary")
    public ResponseEntity <List<PurchaseOrderSummaryDTO>> getPurchaseOrderSummary(){
        return ResponseEntity.ok(purchaseOrderUseCase.getClientPurchaseOrderSummary());
    }

    @Operation(summary = "Mise à jour de Bon de commande", description = "Mettre à jour un Bon de commande existant")
    @PatchMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PurchaseOrderDTO> updatePurchaseOrder (@Valid @ModelAttribute PurchaseOrderUpdateDTO form
            ,@RequestParam(value = "purchaseOrderItemsList", required = true) String purchaseOrderItemsJson)
            throws DataIntegrityViolationException, IOException {
        List<PurchaseOrderItemCreateDTO> purchaseOrderItems = objectMapper.readValue(
                purchaseOrderItemsJson,
                new TypeReference<List<PurchaseOrderItemCreateDTO>>() {}
        );
        System.out.println(purchaseOrderItems);
        form.setPurchaseOrderItems(purchaseOrderItems);
        return ResponseEntity.status(201).body(purchaseOrderUseCase.updateClientPurchaseOrder(form));
    }

    @PatchMapping("/{purchaseOrderId}/status")
    public ResponseEntity<PurchaseOrderDTO> updatePurchaseOrderStatus(
            @PathVariable String purchaseOrderId,
            @Valid @ModelAttribute UpdatePurchaseOrderStatusRequest request
    ) {

        PurchaseOrderDTO updated = purchaseOrderUseCase.updateClientPurchaseOrderStatus(
                UUID.fromString(purchaseOrderId),
                PurchaseOrderStatus.valueOf(request.getStatus())
        );

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Suppression d'un bon de commande")
    public ResponseEntity<Void> deletePurchaseOrder(@Parameter(description = "ID du bon de commande") @PathVariable String id)
    {
        purchaseOrderUseCase.deleteClientPurchaseOrder(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }


    /******** Coummun *****/

    @GetMapping("/partner/{id}")
    @Operation (summary = "Liste des bons de commande d'un partenaire")
    public List<PurchaseOrderPartnerSummaryDTO> getPurchaseOrderListByIdPartner(@Parameter(description = "ID du partenaire") @PathVariable String id,@Parameter(description = "Le type partenaire") @RequestParam String partnerType)
    {
      return  purchaseOrderUseCase.getPurchaseOrdersByPartnerId(UUID.fromString(id),partnerType) ;
    }

    /************ SUPPLIER ************/

    @Operation(summary = "Créer un bon de commande", description = "Ajoute un nouveau bon de commande")
    @PostMapping(path = "/supplier", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PurchaseOrderDTO> createSupplierPurchaseOrder ( @ModelAttribute PurchaseOrderCreateDTO form,
                                                                  @RequestParam(value = "purchaseOrderItemsList", required = true) String purchaseOrderItemsJson)
            throws DataIntegrityViolationException, IOException {
        List<PurchaseOrderItemCreateDTO> purchaseOrderItems = objectMapper.readValue(
                purchaseOrderItemsJson,
                new TypeReference<List<PurchaseOrderItemCreateDTO>>() {}
        );
        System.out.println(purchaseOrderItems);
        form.setPurchaseOrderItems(purchaseOrderItems);
        return ResponseEntity.status(201).body(purchaseOrderUseCase.createSupplierPurchaseOrder(form));
    }
     // test

    @Operation(summary = "Liste des bons de commande")
    @GetMapping("/supplier")
    public ResponseEntity <Page<PurchaseOrderPageItemDTO>> getSupplierPurchaseOrders(@RequestParam(required = false) String keyword, @RequestParam(required = false) String filter,
                                                                             @RequestParam int page )
    {
        return ResponseEntity.ok(purchaseOrderUseCase.getSupplierPurchaseOrders(keyword,filter, page));
    }


    @GetMapping("/supplier/{id}")
    @Operation(summary = "Détails d'un bon de commande")
    public ResponseEntity<PurchaseOrderDTO> getSupplierPurchaseOrderById(@Parameter(description = "ID du bon de commande") @PathVariable String id)
    {
        PurchaseOrderDTO purchaseOrder =  purchaseOrderUseCase.getSupplierPurchaseOrderById(UUID.fromString(id));
        return ResponseEntity.status(201).body(purchaseOrder);
    }
    @GetMapping("/supplier/summary")
    public ResponseEntity <List<PurchaseOrderSummaryDTO>> getSupplierPurchaseOrderSummary(){
        return ResponseEntity.ok(purchaseOrderUseCase.getSupplierPurchaseOrderSummary());
    }

    @Operation(summary = "Mise à jour de Bon de commande", description = "Mettre à jour un Bon de commande existant")
    @PatchMapping(path = "/supplier", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PurchaseOrderDTO> updateSupplierPurchaseOrder (@Valid @ModelAttribute PurchaseOrderUpdateDTO form
            ,@RequestParam(value = "purchaseOrderItemsList", required = true) String purchaseOrderItemsJson)
            throws DataIntegrityViolationException, IOException {
        List<PurchaseOrderItemCreateDTO> purchaseOrderItems = objectMapper.readValue(
                purchaseOrderItemsJson,
                new TypeReference<List<PurchaseOrderItemCreateDTO>>() {}
        );
        System.out.println(purchaseOrderItems);
        form.setPurchaseOrderItems(purchaseOrderItems);
        return ResponseEntity.status(201).body(purchaseOrderUseCase.updateSupplierPurchaseOrder(form));
    }
    @PatchMapping("/supplier/{purchaseOrderId}/status")
    public ResponseEntity<PurchaseOrderDTO> updateSupplierPurchaseOrderStatus(
            @PathVariable String purchaseOrderId,
            @Valid @ModelAttribute UpdatePurchaseOrderStatusRequest request
    ) {

        PurchaseOrderDTO updated = purchaseOrderUseCase.updateSupplierPurchaseOrderStatus(
                UUID.fromString(purchaseOrderId),
                PurchaseOrderStatus.valueOf(request.getStatus())
        );

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/supplier/{id}")
    @Operation(summary = "Suppression d'un bon de commande")
    public ResponseEntity<Void> deleteSupplierPurchaseOrder(@Parameter(description = "ID du bon de commande") @PathVariable String id)
    {
        purchaseOrderUseCase.deleteSupplierPurchaseOrder(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }


}
