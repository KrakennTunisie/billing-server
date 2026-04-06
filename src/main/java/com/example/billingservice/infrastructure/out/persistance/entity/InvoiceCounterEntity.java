package com.example.billingservice.infrastructure.out.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invoice_counters")
@Getter
@Setter
public class InvoiceCounterEntity {
    @Id
    @Column(name = "year_value")
    private Integer year;

    @Column(name = "last_sequence", nullable = false)
    private Long lastSequence;
}
