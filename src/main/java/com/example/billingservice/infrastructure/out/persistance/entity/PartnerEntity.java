package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.enums.PaymentCondition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "partners")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "partner_type",discriminatorType = DiscriminatorType.STRING)
@Setter
@Getter
public abstract class PartnerEntity {

    // Paramétres personnels ────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identifiant unique", example = "550e8400-e29b-41d4-a716-446655440000")
    @Column(name = "id_partner", updatable = false, nullable = false)
    private UUID idPartner;

    @Schema(description = "Status du partenaire", example = "Active || UnActive")
    @Column(name = "active", nullable = false)
    private boolean active;

    @Schema(description = "Portail client", example = "Oui || Non")
    @Column(name = "enablePortal", nullable = false)
    private boolean enablePortal;

    @Schema(description = "Nom du partenaire", example = "Kouka")
    @Column(name = "partner_name")
    private String partnerName;

    @Schema(description = "Situation socaile", example = "Miss")
    @Column(name = "marital_status")
    private String maritalStatus;

    @Schema(description = "Nom du l'entreprise", example = "Kouka")
    @Column(name = "company_name")
    private String companyName;

    @Schema(description = "Nom affichee", example = "Kouka SARL")
    @Column(name = "display_name")
    private String displayName;

    @Schema(description = "Email du partenaire", example = "oumaima@example.com")
    @Column(name = "email", unique = true)
    private String email;

    @Schema(description = "Numéro de téléphone personnel", example = "+33612345678")
    @Column(name = "personnel_phone_number")
    private String personnelPhoneNumber;

    @Schema(description = "Numéro de téléphone professionnelle", example = "+33612345678")
    @Column(name = "professionnal_phone_number")
    private String professionnalPhoneNumber;

    // ── Adresses ────────────────────────────────────────────────
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_address_id")
    private AddressEntity billingAddressEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id")
    private AddressEntity shippingAddressEntity;

    // ── Paramètres financiers ────────────────────────────────────
    @Schema(description = "Langue", example = "FR")
    @Column(name = "language")
    private String language;

    @Schema(description = "Devise", example = "EUR")
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private InvoiceCurrency currency;

    @Schema(description = "Pourcentage tax", example = "19%")
    @Column(name = "tax_rate")
    private String taxRate;

    @Schema(description = "Numéro fiscal", example = "TAX123456")
    @Column(name = "tax_registration_number", unique = true)
    private String taxRegistrationNumber;

    @Schema(description = "Condition de paiement", example = "NET_15")
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_condition")
    private PaymentCondition paymentCondition;

    @Schema(description = "IBAN", example = "FR7630006000011234567890189")
    @Column(name = "iban")
    private String iban;


    // RNE document
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "rne_document_id")
    private List<DocumentEntity> rne;

    // PATENT document
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patente_document_id")
    private DocumentEntity patente;

    // Contact document
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "contract_document_id")
    private List<DocumentEntity>  contract;


    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceEntity> invoice;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuditLogEntity> logs;


}
