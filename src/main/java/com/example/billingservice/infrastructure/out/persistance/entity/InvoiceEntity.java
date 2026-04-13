package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "invoices")
@Getter
@Setter
public class InvoiceEntity extends BaseCommercialDocumentEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idInvoice;

    private Date dueDate;

    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @Enumerated(EnumType.STRING)
    private InvoiceComplianceStatus invoiceComplianceStatus;

    private String complianceQRcode;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "invoice_document_id", referencedColumnName = "idDocument")
    private DocumentEntity invoiceDocument;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "purchase_order_id", nullable = true)
    private PurchaseOrderEntity purchaseOrder;


    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceEventEntity> invoiceEvents = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItemEntity> invoiceItems;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceCreditNoteEntity> invoiceCreditNotes;
}
