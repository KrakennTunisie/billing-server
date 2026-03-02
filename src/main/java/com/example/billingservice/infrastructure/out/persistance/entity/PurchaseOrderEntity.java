package com.example.billingservice.infrastructure.out.persistance.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idPurchaseOrder;


    private String reference;

    private LocalDate orderDate;

    private Double totalAmountExclTax;


    private Double totalAmountInclTax;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<InvoiceEntity> invoices;
}
