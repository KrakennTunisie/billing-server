package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.enums.PaymentCondition;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Modèle partenaire")
public class Partner {
    @Schema(description = "Identifiant unique", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID idPartner;
    @Schema(description = "Status du partenaire", example = "Active || UnActive")
    private boolean active;
    @Schema(description = "Portail client", example = "Oui || Non")
    private boolean enablePortal;
    @Schema(description = "Nom du partenaire", example = "Kouka")
    private String partnerName;
    @Schema(description = "Situation sociale", example = "Miss")
    private String maritalStatus;
    @Schema(description = "Nom du l'entreprise", example = "Kouka")
    private String companyName;
    @Schema(description = "Nom affichee", example = "Kouka SARL")
    private String displayName;
    @Schema(description = "Email du partenaire", example = "oumaima@example.com")
    private String email;
    @Schema(description = "Numéro de téléphone personnel", example = "+33612345678")
    private String personnelPhoneNumber;
    @Schema(description = "Numéro de téléphone professionenelle", example = "+33612345678")
    private String professionnalPhoneNumber;
    @Schema(description = "Addresse de facturation", example = "Nabeul ,city")
    private Address billingAddress;
    @Schema(description = "Addresse de livraison", example = "Nabeul ,city")
    private Address shippingAddress;
    @Schema(description = "Langue", example = "+33612345678")
    private String Language;
    @Schema(description = "Devise", example = "EUR")
    private InvoiceCurrency currency;
    @Schema(description = "Pourcentage tax", example = "19%")
    private String taxRate;
    @Schema(description = "Numéro fiscal", example = "TAX123456")
    private String taxRegistrationNumber;
    @Schema(description = "Condition de paiement", example = "NET_15")
    private PaymentCondition paymentCondition;
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
    @Schema(description = "Liste d'audit pour le client : Tout ce qui est concerne le client : Facture / avoir / contact ")
    private List<AuditLog>  logs;


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
                "idPartner=" + idPartner +
                ", active=" + active +
                ", partnerName='" + partnerName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", personnelPhoneNumber='" + personnelPhoneNumber + '\'' +
                ", professionnalPhoneNumber='" + professionnalPhoneNumber + '\'' +
                ", billingAddress=" + billingAddress +
                ", shippingAddress=" + shippingAddress +
                ", Language='" + Language + '\'' +
                ", currency=" + currency +
                ", TaxRate='" + taxRate + '\'' +
                ", taxRegistrationNumber='" + taxRegistrationNumber + '\'' +
                ", paymentCondition=" + paymentCondition +
                ", iban='" + iban + '\'' +
                ", rne=" + rne +
                ", contract=" + contract +
                ", patente=" + patente +
                ", partnerType=" + partnerType +
                ", invoices=" + invoices +
                '}';
    }
}
