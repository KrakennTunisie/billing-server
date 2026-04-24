package com.example.billingservice.infrastructure.out.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "invoice_credit_notes_items")
@Getter
@Setter
public class InvoiceCreditNoteItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idInvoiceCreditNoteItem;

    // Quantity can be decimal (ex: 1.5 kg, 2.25 hours)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_item_id", referencedColumnName = "idInvoiceItem")
    private InvoiceItemEntity invoiceItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_credit_note_id", nullable = false)
    private InvoiceCreditNoteEntity invoiceCreditNote;


}
