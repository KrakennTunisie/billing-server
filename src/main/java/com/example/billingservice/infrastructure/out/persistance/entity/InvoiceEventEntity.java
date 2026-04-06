package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.InvoiceEventTrigger;
import com.example.billingservice.domain.enums.InvoiceEventType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "invoices_events")
public class InvoiceEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idInvoiceEvent;


    @Enumerated(EnumType.STRING)
    private InvoiceEventType invoiceEventType;

    private Date eventDate;

    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private InvoiceEventTrigger eventTrigger;

    private String triggeredBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceEntity invoice;
}
