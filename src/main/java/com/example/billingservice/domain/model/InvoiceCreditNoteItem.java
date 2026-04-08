package com.example.billingservice.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class InvoiceCreditNoteItem {

    private UUID idInvoiceCreditNoteItem;

    private InvoiceCreditNote invoiceCreditNote;

    private InvoiceItem invoiceItem;

    private Integer quantity;
}
