package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.InvoiceCreditNoteEventType;
import com.example.billingservice.domain.enums.InvoiceEventTrigger;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invoice_credit_note_events")
@Getter
@Setter
public class InvoiceCreditNoteEventEntity extends BaseEventEntity{

    @Enumerated(EnumType.STRING)
    private InvoiceCreditNoteEventType eventType;

    @Enumerated(EnumType.STRING)
    private InvoiceEventTrigger eventTrigger;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "credit_note_id", nullable = false)
    private InvoiceCreditNoteEntity invoiceCreditNote;
}
