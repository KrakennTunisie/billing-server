package com.example.billingservice.domain.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PurchaseOrder {

    private UUID idPurchaseOrder;
    private String reference;
    private Date orderDate;
    private Double totalAmountExclTax;
    private Double totalAmountInclTax;
    private List<Invoice> invoices;

    public PurchaseOrder(String reference, Date orderDate, Double totalAmountExclTax,
                         Double totalAmountInclTax) {
        this.reference = reference;
        this.orderDate = orderDate;
        this.totalAmountExclTax = totalAmountExclTax;
        this.totalAmountInclTax = totalAmountInclTax;
    }

    public PurchaseOrder() {
    }

    public UUID getIdPurchaseOrder() {
        return idPurchaseOrder;
    }

    public void setIdPurchaseOrder(UUID idPurchaseOrder) {
        this.idPurchaseOrder = idPurchaseOrder;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Double getTotalAmountExclTax() {
        return totalAmountExclTax;
    }

    public void setTotalAmountExclTax(Double totalAmountExclTax) {
        this.totalAmountExclTax = totalAmountExclTax;
    }

    public Double getTotalAmountInclTax() {
        return totalAmountInclTax;
    }

    public void setTotalAmountInclTax(Double totalAmountInclTax) {
        this.totalAmountInclTax = totalAmountInclTax;
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
