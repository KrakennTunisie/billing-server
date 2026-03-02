package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.InvoiceEventType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invoices_events")
public class InvoiceEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idInvoiceEvent;



    @Enumerated(EnumType.STRING)
    private InvoiceEventType invoiceEventType;

    private LocalDateTime eventDate;

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceEntity invoice;
}
