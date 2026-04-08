package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.OperationCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "invoice_items")
@Getter
@Setter
public class InvoiceItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idInvoiceItem;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    // Quantity can be decimal (ex: 1.5 kg, 2.25 hours)
    private Integer quantity;

    private Double unityPriceEXclTax;

    private Double vatRate;

    @Enumerated(EnumType.STRING)
    private OperationCategory operationCategory;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceEntity invoice;
}
