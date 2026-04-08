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
public class InvoiceEventEntity  extends BaseEventEntity{


    @Enumerated(EnumType.STRING)
    private InvoiceEventType invoiceEventType;

    @Enumerated(EnumType.STRING)
    private InvoiceEventTrigger eventTrigger;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceEntity invoice;
}
