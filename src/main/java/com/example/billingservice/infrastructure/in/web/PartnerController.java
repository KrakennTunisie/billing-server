package com.example.billingservice.infrastructure.in.web;


import com.example.billingservice.application.ports.in.PartnerDTO;
import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.model.Partner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name= "Partner API", description = "Gestion des paramétres")
@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerUseCase partnerUseCase;
    @Operation(summary = "Créer un partenaire", description = "Ajoute un nouveau partenaire")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Partner.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity <Partner> create (@RequestBody PartnerDTO request)
    {
        return ResponseEntity.status(201).body(partnerUseCase.createPartner(request));
    }
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un partenaire")
    public ResponseEntity<Optional<Partner>> getById(@Parameter(description = "ID du partenaire")@PathVariable String id)
    {
        return ResponseEntity.ok(partnerUseCase.getById(id));
    }
    @Operation(summary = "Liste des partenaires")
    @GetMapping
    public ResponseEntity <List<Partner>> getAll()
    {
        return ResponseEntity.ok(partnerUseCase.getAll());
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Suppression d'un partenaire")
    public ResponseEntity<Void> deletePartner(@Parameter(description = "ID du partenaire") @PathVariable String id)
    {
        partnerUseCase.deletePartner(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    @Operation(summary = "Modification d'un partenaire")
    public ResponseEntity <Partner> update (@Parameter(description = "ID du partenaire") @PathVariable String id ,@RequestBody PartnerDTO request)
    {
       return ResponseEntity.status(201).body(partnerUseCase.updatePartner(id, request)) ;
    }
    @GetMapping("/name/{name}")
    @Operation(summary = "Rechercher par nom")
    public ResponseEntity <Partner> getPartnerByName (@Parameter(description = "Nom du partenaire", example = "Oumaima chelly") @PathVariable String name)
    {
        return ResponseEntity.ok(partnerUseCase.getByName(name));
    }
    @GetMapping("/email/{email}")
    @Operation(summary = "Rechercher par email")
    public ResponseEntity <Partner> getPartnerByEmail( @Parameter(description = "Email", example = "Oumaima@example.com") @PathVariable String email)
    {
        return ResponseEntity.ok(partnerUseCase.getByEmail(email));
    }
    @GetMapping("/taxRegistrationNumber/{number}")
    @Operation(summary = "Rechercher par numéro du tax")
    public ResponseEntity <Partner> getPartnerByTaxNumber(@Parameter(description = "Numéro du tax", example = "TAX12554") @PathVariable String number)
    {
        return ResponseEntity.ok(partnerUseCase.getByTaxRegistrationNumber(number));
    }
    @GetMapping("/partnerType/{type}")
    @Operation(summary = "Rechercher par le type de partenaire")
    public ResponseEntity <List<Partner>> getPartnerByPartnerType(@Parameter(description = "Type de partenaire", example = "Client") @PathVariable String type)
    {
        return ResponseEntity.ok(partnerUseCase.getByPartnerType(PartnerType.valueOf(type)));
    }

}
