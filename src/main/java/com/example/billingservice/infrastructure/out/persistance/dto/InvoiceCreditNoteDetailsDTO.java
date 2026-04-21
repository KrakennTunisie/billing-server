package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.InvoiceComplianceStatus;
import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;
import com.example.billingservice.domain.model.InvoiceCreditNoteEvent;
import com.example.billingservice.domain.model.InvoiceCreditNoteItem;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class InvoiceCreditNoteDetailsDTO {

    private UUID idInvoiceCreditNote;
    private String invoiceCreditNoteNumber;

    private String motif;
    private String description;

    private Date issueDate;

    private InvoiceCreditNoteStatus invoiceCreditNoteStatus;
    private InvoiceComplianceStatus invoiceCreditNoteComplianceStatus;

    private Double total;

    private InvoiceSummaryDTO invoice;

    private DocumentSummaryDTO invoiceCreditNoteDocument;

    private Double totalExclTaxEUR;
    private Double totalInclTaxEUR;
    private Double totalExclTaxTND;
    private Double totalInclTaxTND;

    private List<InvoiceCreditNoteItem> invoiceCreditNoteItems;

    private List<InvoiceCreditNoteEvent> invoiceCreditNoteEvents;
}
