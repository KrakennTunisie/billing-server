package com.example.billingservice.infrastructure.out.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "purchase_order_items")
@Getter
@Setter
public class PurchaseOrderItemEntity extends BaseItemEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idPurchaseOrderItem;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrderEntity purchaseOrder;
}
