package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Modèle Facture")
public class Invoice {

    private UUID idInvoice;
    private String invoiceNumber;
    private Date issueDate;
    private Date dueDate;
    private InvoiceType invoiceType;
    private InvoiceStatus invoiceStatus;
    private InvoiceComplianceStatus invoiceComplianceStatus;
    private InvoiceCurrency invoiceCurrency;
    private Double totalExclTaxEUR;
    private Double totalInclTaxEUR;
    private Double totalExclTaxTND;
    private Double totalInclTaxTND;
    private Double vatRate;
    private PaymentMethod paymentMethod;
    private Date exchangeRateReferenceDate;
    private Double appliedExchangeRate;
    private ExchangeRateSource exchangeRateSource;
    private String complianceQRcode;
    private PurchaseOrder purchaseOrder;
    private Partner partner;

    private List<InvoiceItem> invoiceItems;
    private List<InvoiceEvent> invoiceEvents;
    private Document invoiceDocument;

    public void addEvent(InvoiceEvent event) {
        this.invoiceEvents.add(event);
        event.setInvoice(this);
    }

    public void addItem(InvoiceItem item) {
        invoiceItems.add(item);
        item.setInvoice(this);
    }

    public void removeItem(InvoiceItem item) {
        invoiceItems.remove(item);
        item.setInvoice(null);
    }

    public void clearAndSetItems(List<InvoiceItem> items) {
        for (InvoiceItem item : new ArrayList<>(invoiceItems)) {
            removeItem(item);
        }
        for (InvoiceItem item : items) {
            addItem(item);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Invoice invoice)) return false;
        return Objects.equals(invoiceNumber, invoice.invoiceNumber) && Objects.equals(partner, invoice.partner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceNumber, partner);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "idInvoice=" + idInvoice +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", invoiceType=" + invoiceType +
                ", invoiceStatus=" + invoiceStatus +
                ", invoiceComplianceStatus=" + invoiceComplianceStatus +
                ", invoiceCurrency=" + invoiceCurrency +
                ", totalExclTaxEUR=" + totalExclTaxEUR +
                ", totalInclTaxEUR=" + totalInclTaxEUR +
                ", totalExclTaxTND=" + totalExclTaxTND +
                ", totalInclTaxTND=" + totalInclTaxTND +
                ", vatRate=" + vatRate +
                ", paymentMethod=" + paymentMethod +
                ", exchangeRateReferenceDate=" + exchangeRateReferenceDate +
                ", appliedExchangeRate=" + appliedExchangeRate +
                ", exchangeRateSource=" + exchangeRateSource +
                ", complianceQRcode='" + complianceQRcode + '\'' +
                ", partner=" + partner.toString() +
                ", invoiceEvents=" + invoiceEvents.toString() +
                ", invoiceDocument=" + invoiceDocument.toString() +
                '}';
    }
}
