package com.example.billingservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItem extends BaseItem{

    private UUID idPurchaseOrderItem;

    private PurchaseOrder purchaseOrder;

    public UUID getIdPurchaseOrderItem() {
        return idPurchaseOrderItem;
    }

    public void setIdPurchaseOrderItem(UUID idPurchaseOrderItem) {
        this.idPurchaseOrderItem = idPurchaseOrderItem;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
}
