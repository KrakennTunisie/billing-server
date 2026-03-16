package com.example.billingservice.infrastructure.in.web;


import com.example.billingservice.infrastructure.out.persistance.dto.PartnerDTO;
import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.domain.model.Partner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;






@Tag(name= "Partner API", description = "Gestion des paramétres")
@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerUseCase partnerUseCase;

    /*********** SUPPLIER ************/


    @Operation(summary = "Créer un fournisseur", description = "Ajoute un nouveau partenaire")
    @PostMapping("/supplier")
    public ResponseEntity <Optional<Partner>> createSupplier (@RequestBody PartnerDTO request) throws IOException {
        return ResponseEntity.status(201).body(partnerUseCase.createSupplier(request));
    }
    @GetMapping("/supplier/{id}")
    @Operation(summary = "Récupérer un fournisseur")
    public ResponseEntity<Optional<Partner>> getSupplierById(@Parameter(description = "ID du partenaire")@PathVariable String id)
    {
        return ResponseEntity.ok(partnerUseCase.getSupplierById(id));
    }
    @Operation(summary = "Liste des fournisseurs")
    @GetMapping("/allSuppliers")
    public ResponseEntity <Page<Partner>> getAllSuppliers(@RequestParam(defaultValue = " ")String Keyword,@RequestParam(defaultValue = "Tunisie")String Country,@RequestParam(defaultValue = "5") int page )
    {
        return ResponseEntity.ok(partnerUseCase.getAllSuppliers(Keyword, Country, page));
    }
    @DeleteMapping("/supplier/{id}")
    @Operation(summary = "Suppression d'un fournisseur")
    public ResponseEntity<Void> deleteSupplier(@Parameter(description = "ID du fournisseur") @PathVariable String id)
    {
        partnerUseCase.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/supplier/{id}")
    @Operation(summary = "Modification d'un fournisseur")
    public ResponseEntity <Partner> updateSupplier (@Parameter(description = "ID du fournisseur") @PathVariable String id ,@RequestBody PartnerDTO request)
    {
       return ResponseEntity.status(201).body(partnerUseCase.updateSupplier(id,request)) ;
    }




    /********** CUSTOMER *************/

    @PostMapping("/customer")
    public ResponseEntity <Optional<Partner>> createCustomer (@RequestBody PartnerDTO request) throws IOException {
        return ResponseEntity.status(201).body(partnerUseCase.createCustomer(request));
    }

    @GetMapping("/allCustomers")
    public ResponseEntity <Page<Partner>> getAllCustomers(@RequestParam(defaultValue = " ")String Keyword,@RequestParam(defaultValue = "Tunisie")String Country,@RequestParam(defaultValue = "5") int page )
    {
        return ResponseEntity.ok(partnerUseCase.getAllCustomers(Keyword, Country, page));
    }
    @GetMapping("/customer/{id}")
    @Operation(summary = "Récupérer un client")
    public ResponseEntity<Optional<Partner>> getCustomerById(@Parameter(description = "ID du client")@PathVariable String id)
    {
        return ResponseEntity.ok(partnerUseCase.findCustomerById(id));
    }
    @DeleteMapping("/customer/{id}")
    @Operation(summary = "Suppression d'un client")
    public ResponseEntity<Void> deleteCustomer(@Parameter(description = "ID du client") @PathVariable String id)
    {
        partnerUseCase.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/customer/{id}")
    @Operation(summary = "Modification client")
    public ResponseEntity <Partner> updateCustomer (@Parameter(description = "ID du client") @PathVariable String id ,@RequestBody PartnerDTO request)
    {
        return ResponseEntity.status(201).body(partnerUseCase.updateCustomer(id,request)) ;
    }
}
