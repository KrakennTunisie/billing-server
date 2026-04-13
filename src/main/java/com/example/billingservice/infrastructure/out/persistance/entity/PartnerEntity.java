package com.example.billingservice.infrastructure.out.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "partners")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "partner_type",discriminatorType = DiscriminatorType.STRING)
@Setter
@Getter
public abstract class PartnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idPartner;

    private String name;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    @Column(unique = true)
    private String taxRegistrationNumber;

    private String country;

    private String address;

    @Column(unique = true)
    private String iban;


    // RNE document
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rne_document_id")
    private DocumentEntity rne;

    // PATENT document
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patente_document_id")
    private DocumentEntity patente;

    // Contact document
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contract_document_id")
    private DocumentEntity contract;


    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceEntity> invoice;


}
