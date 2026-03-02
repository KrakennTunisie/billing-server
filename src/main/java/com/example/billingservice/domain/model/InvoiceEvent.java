package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.InvoiceEventType;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class InvoiceEvent {

    private UUID idInvoiceEvent;
    private Invoice invoice;
    private InvoiceEventType invoiceEventType;
    private Date eventDate;
    private String description;

    public InvoiceEvent(Invoice invoice, InvoiceEventType invoiceEventType, Date eventDate, String description) {
        this.invoice = invoice;
        this.invoiceEventType = invoiceEventType;
        this.eventDate = eventDate;
        this.description = description;
    }

    public InvoiceEvent() {
    }

    public UUID getIdInvoiceEvent() {
        return idInvoiceEvent;
    }

    public void setIdInvoiceEvent(UUID idInvoiceEvent) {
        this.idInvoiceEvent = idInvoiceEvent;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public InvoiceEventType getInvoiceEventType() {
        return invoiceEventType;
    }

    public void setInvoiceEventType(InvoiceEventType invoiceEventType) {
        this.invoiceEventType = invoiceEventType;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof InvoiceEvent that)) return false;
        return Objects.equals(invoice, that.invoice) && invoiceEventType == that.invoiceEventType && Objects.equals(eventDate, that.eventDate) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoice, invoiceEventType, eventDate, description);
    }

    @Override
    public String toString() {
        return "InvoiceEvent{" +
                "idInvoiceEvent=" + idInvoiceEvent +
                ", invoice=" + invoice +
                ", invoiceEventType=" + invoiceEventType +
                ", eventDate=" + eventDate +
                ", description='" + description + '\'' +
                '}';
    }
}
