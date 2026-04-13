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
public class PurchaseOrderEntity extends BaseCommercialDocumentEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idPurchaseOrder;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "purchase_order_document_id", referencedColumnName = "idDocument")
    private DocumentEntity purchaseOrderDocument;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<InvoiceEntity> invoices;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItemEntity> purchaseOrderItems;
}
