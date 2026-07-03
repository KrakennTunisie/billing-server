package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem extends BaseItem{

    private UUID idInvoiceItem;
    private Invoice invoice;
    private PurchaseOrderItem purchaseOrderItem;
    private int creditedQuantity;

    private DiscountType discountType;
    private Double discountValue;

    public UUID getIdInvoiceItem() {
        return idInvoiceItem;
    }

    public void setIdInvoiceItem(UUID idInvoiceItem) {
        this.idInvoiceItem = idInvoiceItem;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
