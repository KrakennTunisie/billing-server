package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.InvoiceComplianceStatus;
import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "invoices_credit_notes")
@Getter
@Setter
public class InvoiceCreditNoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idInvoiceCreditNoteEntity;

    private String invoiceCreditNoteNumber;

    private String motif;
    private String description;

    private Date issueDate;

    private String qrCode;

    private InvoiceComplianceStatus complianceStatus;

    private InvoiceCreditNoteStatus invoiceCreditNoteStatus;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "invoice_credit_note_document_id", referencedColumnName = "idDocument")
    private DocumentEntity invoiceCreditNoteDocument;


    @OneToMany(mappedBy = "invoiceCreditNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceCreditNoteEventEntity> invoiceCreditNoteEvents = new ArrayList<>();

    @OneToMany(mappedBy = "invoiceCreditNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceCreditNoteItemEntity> invoiceCreditNoteItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;
}
