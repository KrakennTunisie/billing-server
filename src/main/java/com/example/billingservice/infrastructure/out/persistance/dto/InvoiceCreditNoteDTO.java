package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.InvoiceComplianceStatus;
import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;
import com.example.billingservice.domain.model.InvoiceCreditNoteEvent;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class InvoiceCreditNoteDTO {

    private UUID idInvoiceCreditNote;
    private String invoiceCreditNoteNumber;

    private String motif;
    private String description;

    private Date issueDate;

    private InvoiceCreditNoteStatus invoiceCreditNoteStatus;
    private InvoiceComplianceStatus invoiceCreditNoteComplianceStatus;

    private Double total;

    private InvoiceSummaryDTO invoice;

    private List<InvoiceCreditNoteEvent> invoiceCreditNoteEvents;
}
