package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.model.Invoice;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "invoices_credit_notes")
public class InvoiceCreditNoteEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idInvoiceCreditNoteEntity;

    private String motif;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceEntity invoice;
}
