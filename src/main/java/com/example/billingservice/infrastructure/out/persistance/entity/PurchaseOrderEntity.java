package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.PurchaseOrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@DiscriminatorColumn(name = "purchaseOrder_type",discriminatorType = DiscriminatorType.STRING)
public class PurchaseOrderEntity extends BaseCommercialDocumentEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idPurchaseOrder;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "purchase_order_document_id", referencedColumnName = "idDocument")
    private DocumentEntity purchaseOrderDocument;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<InvoiceEntity> invoices;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "La status de bon commande est obligatoire")
    private PurchaseOrderStatus purchaseOrderStatus;


    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItemEntity> purchaseOrderItems;
}
