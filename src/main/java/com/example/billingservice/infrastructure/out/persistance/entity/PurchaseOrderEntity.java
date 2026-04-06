package com.example.billingservice.infrastructure.out.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
public class PurchaseOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idPurchaseOrder;


    private String reference;

    private Date orderDate;

    private Double totalAmountExclTax;


    private Double totalAmountInclTax;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<InvoiceEntity> invoices;
}
