package com.example.billingservice.infrastructure.out.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "invoice_items")
@Getter
@Setter
public class InvoiceItemEntity extends BaseItemEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idInvoiceItem;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int creditedQuantity;

    @ManyToOne
    private PurchaseOrderItemEntity purchaseOrderItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "invoice_id", nullable = true)
    private InvoiceEntity invoice;
}
