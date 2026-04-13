package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Modèle Facture")
public class Invoice extends BaseCommercialDocument{

    private UUID idInvoice;
    private Date dueDate;
    private InvoiceType invoiceType;
    private InvoiceStatus invoiceStatus;
    private InvoiceComplianceStatus invoiceComplianceStatus;
    private String complianceQRcode;
    private PurchaseOrder purchaseOrder;

    private List<InvoiceItem> invoiceItems;
    private List<InvoiceEvent> invoiceEvents;
    private List<InvoiceCreditNote> invoiceCreditNotes;
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



}
