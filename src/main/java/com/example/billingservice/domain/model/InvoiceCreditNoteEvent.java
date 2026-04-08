package com.example.billingservice.domain.model;


import com.example.billingservice.domain.enums.InvoiceCreditNoteEventType;
import com.example.billingservice.domain.enums.InvoiceEventTrigger;
import com.example.billingservice.domain.enums.InvoiceEventType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Modèle Evenement Facture d'avoir")
public class InvoiceCreditNoteEvent {
    private UUID idInvoiceEvent;
    private InvoiceCreditNote invoiceCreditNote;
    private InvoiceCreditNoteEventType invoiceCreditNoteEventType;
    private Date eventDate;
    private String description;
    private InvoiceEventTrigger eventTrigger;
    private String triggeredBy;

}
