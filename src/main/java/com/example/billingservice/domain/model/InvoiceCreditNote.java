package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.*;

import java.util.Date;
import java.util.Objects;

public class InvoiceCreditNote extends Invoice{

    private String motif;
    private String description;
    private Invoice invoice;



    public InvoiceCreditNote(String invoiceNumber, Date issueDate, Date dueDate, InvoiceType invoiceType,
                             InvoiceStatus invoiceStatus, InvoiceComplianceStatus invoiceComplianceStatus,
                             Double totalExclTaxEUR, Double totalInclTaxEUR, Double totalExclTaxTND,
                             Double totalInclTaxTND, Double vatRate, PaymentMethod paymentMethod, String creditedAccount,
                             Date exchangeRateReferenceDate, Double appliedExchangeRate, ExchangeRateSource exchangeRateSource,
                             String complianceQRcode, Invoice invoice, String motif, String description) {

        super(invoiceNumber, issueDate, dueDate, invoiceType, invoiceStatus, invoiceComplianceStatus, totalExclTaxEUR,
                totalInclTaxEUR, totalExclTaxTND, totalInclTaxTND, vatRate, paymentMethod, creditedAccount,
                exchangeRateReferenceDate, appliedExchangeRate, exchangeRateSource, complianceQRcode);
        this.motif = motif;
        this.description = description;
        this.invoice = invoice;
    }

    public InvoiceCreditNote(String invoiceNumber, Date issueDate, Date dueDate, InvoiceType invoiceType,
                             InvoiceStatus invoiceStatus, InvoiceComplianceStatus invoiceComplianceStatus,
                             Double totalExclTaxEUR, Double totalInclTaxEUR, Double totalExclTaxTND, Double totalInclTaxTND,
                             Double vatRate, PaymentMethod paymentMethod, String creditedAccount,
                             Date exchangeRateReferenceDate, Double appliedExchangeRate, ExchangeRateSource exchangeRateSource,
                             String complianceQRcode, PurchaseOrder purchaseOrder, Invoice invoice, String motif,
                             String description) {

        super(invoiceNumber, issueDate, dueDate, invoiceType, invoiceStatus, invoiceComplianceStatus, totalExclTaxEUR,
                totalInclTaxEUR, totalExclTaxTND, totalInclTaxTND, vatRate, paymentMethod, creditedAccount,
                exchangeRateReferenceDate, appliedExchangeRate, exchangeRateSource, complianceQRcode, purchaseOrder);
        this.motif = motif;
        this.description = description;
        this.invoice = invoice;

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
