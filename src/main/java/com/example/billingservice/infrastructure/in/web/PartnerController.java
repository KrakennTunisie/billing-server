package com.example.billingservice.infrastructure.in.web;


import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Partner;
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
import java.util.List;
import java.util.Optional;






@Tag(name= "Partner API", description = "Gestion des clients et fournisseurs")
@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerUseCase partnerUseCase;

    /*********** SUPPLIER ************/


    @Operation(summary = "Créer un fournisseur", description = "Ajoute un nouveau fournisseur")
    @PostMapping(path = "/suppliers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity <Partner> createSupplier (@Valid @ModelAttribute PartnerForm form) throws IOException,
            DataIntegrityViolationException {
        return ResponseEntity.status(201).body(partnerUseCase.createSupplier(form));
    }


    @GetMapping("/suppliers/{id}")
    @Operation(summary = "Récupérer un fournisseur")
    public ResponseEntity<Optional<Partner>> getSupplierById(@Parameter(description = "ID du partenaire")@PathVariable String id)
    {
        return ResponseEntity.ok(partnerUseCase.getSupplierById(id));
    }
    @GetMapping("/suppliers/existByEmail/{email}")
    @Operation(summary = "Récupérer un fournisseur")
    public ResponseEntity<Optional<Partner>> supplierExistByEmail(@Parameter(description = "Email du fournisseur")@PathVariable String email)
    {
        return ResponseEntity.ok(partnerUseCase.findSupplierExistsByEmail(email));
    }

    @GetMapping("/name/{id}")
    @Operation(summary = "Récupérer un fournisseur")
    public ResponseEntity<Partner> getSupplierByName(@Parameter(description = "ID du partenaire") @PathVariable String id) {
        return partnerUseCase.getSupplierByName(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> BillingException.notFound("Fournisseur", id));
    }

    @GetMapping("/supplier-item/{email}")
    @Operation(summary = "Récupérer un fournisseur")
    public ResponseEntity<PartnerItemDTO> getSupplierItemByEmail(
            @Parameter(description = "Email du partenaire")@PathVariable String email)
    {
        return ResponseEntity.ok(partnerUseCase.getSupplierByEmail(email));
    }


    @Operation(summary = "Liste des fournisseurs")
    @GetMapping("/suppliers")
    public ResponseEntity <Page<PartnerItemDTO>> getAllSuppliers(@RequestParam(required = false) String keyword,
                                                                 @RequestParam(required = false) String filter,
                                                                 @RequestParam int page )
    {
        return ResponseEntity.ok(partnerUseCase.getAllSuppliers(keyword, filter, page));
    }


    @DeleteMapping("/suppliers/{id}")
    @Operation(summary = "Suppression d'un fournisseur")
    public ResponseEntity<Void> deleteSupplier(@Parameter(description = "ID du fournisseur") @PathVariable String id)
    {
        partnerUseCase.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping(path ="/suppliers/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Modification d'un fournisseur")
    public ResponseEntity <Partner> updateSupplier (@Parameter(description = "ID du fournisseur") @PathVariable String id ,
                                                    @ModelAttribute UpdatePartnerDTO request)
    {
       return ResponseEntity.status(201).body(partnerUseCase.updateSupplier(id,request)) ;
    }

    @PatchMapping(path = "/suppliers/updateStatus/{id}")
    @Operation(summary = "Modification du statut fournisseur activé / désactivé")
    public ResponseEntity<Void> updateStatusSupplier(
            @Parameter(description = "ID du fournisseur")
            @PathVariable String id,
            @RequestParam Boolean statusClient
    ) {
        partnerUseCase.updateSupplierStatus(id, statusClient);
        return ResponseEntity.noContent().build();
    }




    /********** CUSTOMER *************/
    @Operation(summary = "Créer un client", description = "Ajoute un nouveau client")
    @PostMapping(path = "/clients", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity <Optional<Partner>> createCustomer (@ModelAttribute PartnerForm form) throws IOException {
        return ResponseEntity.status(201).body(partnerUseCase.createCustomer(form));
    }

    @GetMapping("/clients")
    public ResponseEntity <Page<PartnerItemDTO>> getAllCustomers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String filter,
            @RequestParam int page ) {

        return ResponseEntity.ok(partnerUseCase.getAllCustomers(keyword, filter, page));
    }

    @GetMapping("/clients-summary")
    public ResponseEntity <List<PartnerSummaryDTO>> getAllCustomers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String filter) {

        return ResponseEntity.ok(partnerUseCase.getSummaryClients(keyword, filter));
    }


    @GetMapping("/clients/{id}")
    @Operation(summary = "Récupérer un client")
    public ResponseEntity<Optional<Partner>> getCustomerById(@Parameter(description = "ID du client")@PathVariable String id)
    {
        return ResponseEntity.ok(partnerUseCase.findCustomerById(id));
    }


    @DeleteMapping("/clients/{id}")
    @Operation(summary = "Suppression d'un client")
    public ResponseEntity<Void> deleteCustomer(@Parameter(description = "ID du client") @PathVariable String id)
    {
        partnerUseCase.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping(path = "/clients/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Modification client")
    public ResponseEntity <Partner> updateCustomer (@Parameter(description = "ID du client") @PathVariable String id ,
                                                    @ModelAttribute UpdatePartnerDTO request)
    {
        return ResponseEntity.status(201).body(partnerUseCase.updateCustomer(id,request)) ;
    }

    @PatchMapping(path = "/clients/updateStatus/{id}")
    @Operation(summary = "Modification du statut client activé / désactivé")
    public ResponseEntity<Void> updateStatusCustomer(
            @Parameter(description = "ID du client")
            @PathVariable String id,
            @RequestParam Boolean statusClient
    ) {
        partnerUseCase.updateCustomerStatus(id, statusClient);
        return ResponseEntity.noContent().build();
    }
}
