package com.example.billingservice.infrastructure.out.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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


    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL)
    private List<InvoiceEntity> invoice;


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
