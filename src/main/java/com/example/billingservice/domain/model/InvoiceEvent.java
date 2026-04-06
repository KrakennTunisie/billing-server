package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.InvoiceEventTrigger;
import com.example.billingservice.domain.enums.InvoiceEventType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Modèle Evenement Facture")
public class InvoiceEvent {

    private UUID idInvoiceEvent;
    private Invoice invoice;
    private InvoiceEventType invoiceEventType;
    private Date eventDate;
    private String description;
    private InvoiceEventTrigger eventTrigger;
    private String triggeredBy;


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
