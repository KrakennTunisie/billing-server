package com.example.billingservice.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
public class PurchaseOrder {

    private UUID idPurchaseOrder;
    private String reference;
    private Date orderDate;
    private Double totalAmountExclTax;
    private Double totalAmountInclTax;
    private List<Invoice> invoices;

    public PurchaseOrder(UUID idPurchaseOrder, String reference, Date orderDate, Double totalAmountExclTax,
                         Double totalAmountInclTax, List<Invoice> invoices) {
        this.idPurchaseOrder = idPurchaseOrder;
        this.reference = reference;
        this.orderDate = orderDate;
        this.totalAmountExclTax = totalAmountExclTax;
        this.totalAmountInclTax = totalAmountInclTax;
        this.invoices = invoices;
    }

    public PurchaseOrder() {
    }




    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PurchaseOrder that)) return false;
        return Objects.equals(idPurchaseOrder, that.idPurchaseOrder)
                && Objects.equals(reference, that.reference)
                && Objects.equals(orderDate, that.orderDate)
                && Objects.equals(totalAmountExclTax, that.totalAmountExclTax)
                && Objects.equals(totalAmountInclTax, that.totalAmountInclTax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPurchaseOrder, reference, orderDate, totalAmountExclTax,
                totalAmountInclTax);
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "idPurchaseOrder=" + idPurchaseOrder +
                ", reference='" + reference + '\'' +
                ", orderDate=" + orderDate +
                ", totalAmountExclTax=" + totalAmountExclTax +
                ", totalAmountInclTax=" + totalAmountInclTax +
                '}';
    }
}
