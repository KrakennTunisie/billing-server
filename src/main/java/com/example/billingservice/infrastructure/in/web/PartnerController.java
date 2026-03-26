package com.example.billingservice.infrastructure.in.web;


import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerForm;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.UpdatePartnerDTO;
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
import java.util.Optional;






@Tag(name= "Partner API", description = "Gestion des paramétres")
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


    @Operation(summary = "Liste des fournisseurs")
    @GetMapping("/suppliers")
    public ResponseEntity <Page<PartnerItemDTO>> getAllSuppliers(@RequestParam(required = false) String keyword,
                                                                 @RequestParam(required = false) String country,
                                                                 @RequestParam int page )
    {
        return ResponseEntity.ok(partnerUseCase.getAllSuppliers(keyword, country, page));
    }


    @DeleteMapping("/suppliers/{id}")
    @Operation(summary = "Suppression d'un fournisseur")
    public ResponseEntity<Void> deleteSupplier(@Parameter(description = "ID du fournisseur") @PathVariable String id)
    {
        partnerUseCase.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/suppliers/{id}")
    @Operation(summary = "Modification d'un fournisseur")
    public ResponseEntity <Partner> updateSupplier (@Parameter(description = "ID du fournisseur") @PathVariable String id ,
                                                    @RequestBody UpdatePartnerDTO request)
    {
       return ResponseEntity.status(201).body(partnerUseCase.updateSupplier(id,request)) ;
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
            @RequestParam(required = false) String country,
            @RequestParam int page ) {

        return ResponseEntity.ok(partnerUseCase.getAllCustomers(keyword, country, page));
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


    @PatchMapping("/clients/{id}")
    @Operation(summary = "Modification client")
    public ResponseEntity <Partner> updateCustomer (@Parameter(description = "ID du client") @PathVariable String id ,
                                                    @RequestBody UpdatePartnerDTO request)
    {
        return ResponseEntity.status(201).body(partnerUseCase.updateCustomer(id,request)) ;
    }
}
