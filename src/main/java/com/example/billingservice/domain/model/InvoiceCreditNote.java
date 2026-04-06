package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class InvoiceCreditNote extends Invoice{

    private String motif;
    private String description;
    private Invoice invoice;

    public InvoiceCreditNote(UUID idInvoice, String invoiceNumber, Date issueDate, Date dueDate, InvoiceType invoiceType, InvoiceStatus invoiceStatus, InvoiceComplianceStatus invoiceComplianceStatus, InvoiceCurrency invoiceCurrency, Double totalExclTaxEUR, Double totalInclTaxEUR, Double totalExclTaxTND, Double totalInclTaxTND, Double vatRate, PaymentMethod paymentMethod, Date exchangeRateReferenceDate, Double appliedExchangeRate, ExchangeRateSource exchangeRateSource, String complianceQRcode, PurchaseOrder purchaseOrder, Partner partner, List<InvoiceItem> invoiceItems, List<InvoiceEvent> invoiceEvents, Document invoiceDocument) {
        super(idInvoice, invoiceNumber, issueDate, dueDate, invoiceType, invoiceStatus, invoiceComplianceStatus, invoiceCurrency, totalExclTaxEUR, totalInclTaxEUR, totalExclTaxTND, totalInclTaxTND, vatRate, paymentMethod, exchangeRateReferenceDate, appliedExchangeRate, exchangeRateSource, complianceQRcode, purchaseOrder, partner, invoiceItems, invoiceEvents, invoiceDocument);
    }


    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof InvoiceCreditNote that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(motif, that.motif) && Objects.equals(description, that.description)
                && Objects.equals(invoice, that.invoice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), motif, description, invoice);
    }

    @Override
    public String toString() {
        return "InvoiceCreditNote{" +
                "motif='" + motif + '\'' +
                ", description='" + description + '\'' +
                ", invoice=" + invoice.toString() +
                '}';
    }
}
