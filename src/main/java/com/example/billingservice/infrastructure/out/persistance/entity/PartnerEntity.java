package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.PartnerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "partners")
@Setter
@Getter
public class PartnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idPartner;

    private String name;

    private String email;

    private String phoneNumber;

    private String taxRegistrationNumber;

    private String country;

    private String address;

    private String iban;

    @Enumerated(EnumType.STRING)
    private PartnerType partnerType;


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


    @OneToMany(mappedBy = "partner")
    private List<InvoiceEntity> invoice;
}
