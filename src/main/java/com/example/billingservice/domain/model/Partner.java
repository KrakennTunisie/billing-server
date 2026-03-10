package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.PartnerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "Modèle partenaire")
public class Partner {
    @Schema(description = "Identifiant unique", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID idPartner;
    @Schema(description = "Nom du partenaire", example = "Oumaima chelly")
    private String name;
    @Schema(description = "Email du partenaire", example = "oumaima@example.com")
    private String email;
    @Schema(description = "Numéro de téléphone", example = "+33612345678")
    private String phoneNumber;
    @Schema(description = "Numéro fiscal", example = "TAX123456")
    private String taxRegistrationNumber;
    @Schema(description = "Pays", example = "Tunisie")
    private String country;
    @Schema(description = "Adresse", example = "12 Rue de Paris")
    private String address;
    @Schema(description = "IBAN", example = "FR7630006000011234567890189")
    private String iban;
    @Schema(description = "Document RNE")
    private Document rne;
    @Schema(description = "Document Contrat")
    private Document contract;
    @Schema(description = "Document Patente")
    private Document patente;
    @Schema(description = "Type de partenaire", example = "SUPPLIER")
    private PartnerType partnerType;
    @Schema(description = "Liste des factures d'un partenaire")
    private List<Invoice> invoices;


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Partner partner)) return false;
        return Objects.equals(taxRegistrationNumber, partner.taxRegistrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(taxRegistrationNumber);
    }

    @Override
    public String toString() {
        return "Partner{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", taxRegistrationNumber='" + taxRegistrationNumber + '\'' +
                ", country='" + country + '\'' +
                ", adress='" + address + '\'' +
                ", iban=" + iban +
                ", rne=" + rne +
                ", contact=" + contract +
                ", partnerType=" + partnerType +
                '}';
    }
}
