package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Invoice {

    private UUID idInvoice;
    private String invoiceNumber;
    private Date issueDate;
    private Date dueDate;
    private InvoiceType invoiceType;
    private InvoiceStatus invoiceStatus;
    private InvoiceComplianceStatus invoiceComplianceStatus;
    private Double totalExclTaxEUR;
    private Double totalInclTaxEUR;
    private Double totalExclTaxTND;
    private Double totalInclTaxTND;
    private Double vatRate;
    private PaymentMethod paymentMethod;
    private String creditedAccount;
    private Date exchangeRateReferenceDate;
    private Double appliedExchangeRate;
    private ExchangeRateSource exchangeRateSource;
    private String complianceQRcode;
    private PurchaseOrder purchaseOrder;
    private Partner partner;

    private List<InvoiceItem> invoiceItems;
    private List<InvoiceEvent> invoiceEvents;
    private Document invoiceDocument;

    public Invoice(String invoiceNumber, Date issueDate, Date dueDate, InvoiceType invoiceType,
                   InvoiceStatus invoiceStatus, InvoiceComplianceStatus invoiceComplianceStatus,
                   Double totalExclTaxEUR, Double totalInclTaxEUR, Double totalExclTaxTND,
                   Double totalInclTaxTND, Double vatRate, PaymentMethod paymentMethod,
                   String creditedAccount, Date exchangeRateReferenceDate, Double appliedExchangeRate,
                   ExchangeRateSource exchangeRateSource, String complianceQRcode) {
        this.invoiceNumber = invoiceNumber;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.invoiceType = invoiceType;
        this.invoiceStatus = invoiceStatus;
        this.invoiceComplianceStatus = invoiceComplianceStatus;
        this.totalExclTaxEUR = totalExclTaxEUR;
        this.totalInclTaxEUR = totalInclTaxEUR;
        this.totalExclTaxTND = totalExclTaxTND;
        this.totalInclTaxTND = totalInclTaxTND;
        this.vatRate = vatRate;
        this.paymentMethod = paymentMethod;
        this.creditedAccount = creditedAccount;
        this.exchangeRateReferenceDate = exchangeRateReferenceDate;
        this.appliedExchangeRate = appliedExchangeRate;
        this.exchangeRateSource = exchangeRateSource;
        this.complianceQRcode = complianceQRcode;
    }

    public Invoice(String invoiceNumber, Date issueDate, Date dueDate, InvoiceType invoiceType,
                   InvoiceStatus invoiceStatus, InvoiceComplianceStatus invoiceComplianceStatus,
                   Double totalExclTaxEUR, Double totalInclTaxEUR, Double totalExclTaxTND,
                   Double totalInclTaxTND, Double vatRate, PaymentMethod paymentMethod,
                   String creditedAccount, Date exchangeRateReferenceDate, Double appliedExchangeRate,
                   ExchangeRateSource exchangeRateSource, String complianceQRcode, PurchaseOrder purchaseOrder) {
        this.invoiceNumber = invoiceNumber;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.invoiceType = invoiceType;
        this.invoiceStatus = invoiceStatus;
        this.invoiceComplianceStatus = invoiceComplianceStatus;
        this.totalExclTaxEUR = totalExclTaxEUR;
        this.totalInclTaxEUR = totalInclTaxEUR;
        this.totalExclTaxTND = totalExclTaxTND;
        this.totalInclTaxTND = totalInclTaxTND;
        this.vatRate = vatRate;
        this.paymentMethod = paymentMethod;
        this.creditedAccount = creditedAccount;
        this.exchangeRateReferenceDate = exchangeRateReferenceDate;
        this.appliedExchangeRate = appliedExchangeRate;
        this.exchangeRateSource = exchangeRateSource;
        this.complianceQRcode = complianceQRcode;
        this.purchaseOrder = purchaseOrder;
    }

    public UUID getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(UUID idInvoice) {
        this.idInvoice = idInvoice;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public InvoiceComplianceStatus getInvoiceComplianceStatus() {
        return invoiceComplianceStatus;
    }

    public void setInvoiceComplianceStatus(InvoiceComplianceStatus invoiceComplianceStatus) {
        this.invoiceComplianceStatus = invoiceComplianceStatus;
    }

    public Double getTotalExclTaxEUR() {
        return totalExclTaxEUR;
    }

    public void setTotalExclTaxEUR(Double totalExclTaxEUR) {
        this.totalExclTaxEUR = totalExclTaxEUR;
    }

    public Double getTotalInclTaxEUR() {
        return totalInclTaxEUR;
    }

    public void setTotalInclTaxEUR(Double totalInclTaxEUR) {
        this.totalInclTaxEUR = totalInclTaxEUR;
    }

    public Double getTotalExclTaxTND() {
        return totalExclTaxTND;
    }

    public void setTotalExclTaxTND(Double totalExclTaxTND) {
        this.totalExclTaxTND = totalExclTaxTND;
    }

    public Double getTotalInclTaxTND() {
        return totalInclTaxTND;
    }

    public void setTotalInclTaxTND(Double totalInclTaxTND) {
        this.totalInclTaxTND = totalInclTaxTND;
    }

    public Double getVatRate() {
        return vatRate;
    }

    public void setVatRate(Double vatRate) {
        this.vatRate = vatRate;
    }

    public PaymentMethod getPayementMethod() {
        return paymentMethod;
    }

    public void setPayementMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCreditedAccount() {
        return creditedAccount;
    }

    public void setCreditedAccount(String creditedAccount) {
        this.creditedAccount = creditedAccount;
    }

    public Date getExchangeRateReferenceDate() {
        return exchangeRateReferenceDate;
    }

    public void setExchangeRateReferenceDate(Date exchangeRateReferenceDate) {
        this.exchangeRateReferenceDate = exchangeRateReferenceDate;
    }

    public Double getAppliedExchangeRate() {
        return appliedExchangeRate;
    }

    public void setAppliedExchangeRate(Double appliedExchangeRate) {
        this.appliedExchangeRate = appliedExchangeRate;
    }

    public ExchangeRateSource getExchangeRateSource() {
        return exchangeRateSource;
    }

    public void setExchangeRateSource(ExchangeRateSource exchangeRateSource) {
        this.exchangeRateSource = exchangeRateSource;
    }

    public String getComplianceQRcode() {
        return complianceQRcode;
    }

    public void setComplianceQRcode(String complianceQRcode) {
        this.complianceQRcode = complianceQRcode;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Document getInvoiceDocument() {
        return invoiceDocument;
    }

    public void setInvoiceDocument(Document invoiceDocument) {
        this.invoiceDocument = invoiceDocument;
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
                ", totalExclTaxEUR=" + totalExclTaxEUR +
                ", totalInclTaxEUR=" + totalInclTaxEUR +
                ", totalExclTaxTND=" + totalExclTaxTND +
                ", totalInclTaxTND=" + totalInclTaxTND +
                ", vatRate=" + vatRate +
                ", payementMethod=" + paymentMethod +
                ", creditedAccount='" + creditedAccount + '\'' +
                ", exchangeRateReferenceDate=" + exchangeRateReferenceDate +
                ", appliedExchangeRate=" + appliedExchangeRate +
                ", exchangeRateSource=" + exchangeRateSource +
                ", complianceQRcode='" + complianceQRcode + '\'' +
                ", purchaseOrder=" + purchaseOrder.toString() +
                ", partner=" + partner.toString() +
                '}';
    }


}
