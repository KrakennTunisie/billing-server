package com.example.billingservice.infrastructure.out.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public class BaseEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idInvoiceEvent;

    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;

    @Column(length = 1000)
    private String description;

    private String triggeredBy;
}
