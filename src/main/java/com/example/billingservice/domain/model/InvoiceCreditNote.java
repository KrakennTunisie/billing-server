package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.InvoiceComplianceStatus;
import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Modèle Facture d'Avoir")
public class InvoiceCreditNote {

    private UUID idInvoiceCreditNote;

    private String invoiceCreditNoteNumber;

    private Date issueDate;

    private String motif;
    private String description;

    private String qrCode;

    private InvoiceComplianceStatus complianceStatus;

    private InvoiceCreditNoteStatus invoiceCreditNoteStatus;

    private Invoice invoice;

    private Document invoiceCreditNoteDocument;

    private List<InvoiceCreditNoteItem> invoiceCreditNoteItems;

    private List<InvoiceCreditNoteEvent> invoiceCreditNoteEvents;
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
