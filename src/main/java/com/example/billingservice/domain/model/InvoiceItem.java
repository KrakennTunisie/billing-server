package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.OperationCategory;

import java.util.Objects;
import java.util.UUID;

public class InvoiceItem {

    private UUID idInvoiceItem;
    private String description;
    private Integer quantity;
    private Double unityPriceEXclTax;
    private Double vatRate;
    private Double itemTotalExclTax;
    private Double itemTaxAmount;
    private Double itemTotalInclTax;
    private OperationCategory operationCategory;
    private Invoice invoice;

    public InvoiceItem(String description, Integer quantity, Double unityPriceEXclTax,
                       Double vatRate, Double itemTotalExclTax, Double itemTaxAmount,
                       Double itemTotalInclTax, OperationCategory operationCategory, Invoice invoice) {
        this.description = description;
        this.quantity = quantity;
        this.unityPriceEXclTax = unityPriceEXclTax;
        this.vatRate = vatRate;
        this.itemTotalExclTax = itemTotalExclTax;
        this.itemTaxAmount = itemTaxAmount;
        this.itemTotalInclTax = itemTotalInclTax;
        this.operationCategory = operationCategory;
        this.invoice = invoice;
    }

    public InvoiceItem() {
    }

    public UUID getIdInvoiceItem() {
        return idInvoiceItem;
    }

    public void setIdInvoiceItem(UUID idInvoiceItem) {
        this.idInvoiceItem = idInvoiceItem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnityPriceEXclTax() {
        return unityPriceEXclTax;
    }

    public void setUnityPriceEXclTax(Double unityPriceEXclTax) {
        this.unityPriceEXclTax = unityPriceEXclTax;
    }

    public Double getVatRate() {
        return vatRate;
    }

    public void setVatRate(Double vatRate) {
        this.vatRate = vatRate;
    }

    public Double getItemTotalExclTax() {
        return itemTotalExclTax;
    }

    public void setItemTotalExclTax(Double itemTotalExclTax) {
        this.itemTotalExclTax = itemTotalExclTax;
    }

    public Double getItemTaxAmount() {
        return itemTaxAmount;
    }

    public void setItemTaxAmount(Double itemTaxAmount) {
        this.itemTaxAmount = itemTaxAmount;
    }

    public Double getItemTotalInclTax() {
        return itemTotalInclTax;
    }

    public void setItemTotalInclTax(Double itemTotalInclTax) {
        this.itemTotalInclTax = itemTotalInclTax;
    }

    public OperationCategory getOperationCategory() {
        return operationCategory;
    }

    public void setOperationCategory(OperationCategory operationCategory) {
        this.operationCategory = operationCategory;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof InvoiceItem that)) return false;
        return Objects.equals(description, that.description) && Objects.equals(quantity, that.quantity) && Objects.equals(unityPriceEXclTax, that.unityPriceEXclTax) && Objects.equals(vatRate, that.vatRate) && Objects.equals(itemTotalExclTax, that.itemTotalExclTax) && Objects.equals(itemTaxAmount, that.itemTaxAmount) && Objects.equals(itemTotalInclTax, that.itemTotalInclTax) && operationCategory == that.operationCategory && Objects.equals(invoice, that.invoice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, quantity, unityPriceEXclTax, vatRate, itemTotalExclTax, itemTaxAmount, itemTotalInclTax, operationCategory, invoice);
    }

    @Override
    public String toString() {
        return "InvoiceItem{" +
                "idInvoiceItem=" + idInvoiceItem +
                ", description='" + description + '\'' +
                ", quantity='" + quantity + '\'' +
                ", unityPriceEXclTax=" + unityPriceEXclTax +
                ", vatRate=" + vatRate +
                ", itemTotalExclTax=" + itemTotalExclTax +
                ", itemTaxAmount=" + itemTaxAmount +
                ", itemTotalInclTax=" + itemTotalInclTax +
                ", operationCategory=" + operationCategory +
                ", invoice=" + invoice.toString() +
                '}';
    }
}
