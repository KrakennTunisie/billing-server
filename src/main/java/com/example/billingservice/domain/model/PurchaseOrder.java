package com.example.billingservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class PurchaseOrder extends BaseCommercialDocument{

    private UUID idPurchaseOrder;
    private List<Invoice> invoices;
    private List<PurchaseOrderItem> purchaseOrderItems;
    private Document purchaseOrderDocument;
}
