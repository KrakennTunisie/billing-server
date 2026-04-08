package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Builder
@Getter
public class InvoiceCreditNotePageItemDTO {
    private UUID idInvoiceCreditNote;
    private String invoiceCreditNoteNumber;
    private Date issueDate;
    private InvoiceCreditNoteStatus invoiceCreditNoteStatus;
    private InvoiceComplianceStatus invoiceCreditNoteComplianceStatus;
    private Double total;
    private InvoiceSummaryDTO invoice;
}
