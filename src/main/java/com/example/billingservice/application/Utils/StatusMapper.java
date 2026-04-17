package com.example.billingservice.application.Utils;

import com.example.billingservice.domain.enums.InvoiceComplianceStatus;
import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;
import com.example.billingservice.domain.enums.InvoiceStatus;

public final class StatusMapper {

    public static String mapInvoiceStatusToFrench(InvoiceStatus status) {
        if (status == null) return "";

        return switch (status) {
            case DRAFT -> "Brouillon";
            case TO_PAY -> "À payer";
            case TO_COLLECT -> "À encaisser";
            case PAID -> "Payée";
            case CANCELLED -> "Annulée";
        };
    }

    public static String mapCreditNoteStatusToFrench(InvoiceCreditNoteStatus status) {
        if (status == null) return "";

        return switch (status) {
            case REFUNDED -> "Remboursée";
            case NOT_REFUNDED -> "Non remboursée";
            case PENDING -> "En attente";
            case CANCELLED -> "Annulée";
            case DRAFT -> "Brouillon";
        };
    }

    public static String mapComplianceStatusToFrench(InvoiceComplianceStatus status) {
        if (status == null) return "";

        return switch (status) {
            case RECEIVED -> "Reçue";
            case SIGNING_PENDING -> "Signature en attente";
            case SIGNING_FAILED -> "Échec de signature";
            case SIGNING_SUCCEEDED -> "Signée";
            case TTN_PENDING -> "TTN en attente";
            case TTN_SUBMITTED -> "TTN soumise";
            case TTN_ACCEPTED -> "TTN acceptée";
            case TTN_REJECTED -> "TTN rejetée";
            case COMPLETED -> "Terminée";
            case FAILED -> "Échec";
            case CANCELLED -> "Annulée";
        };
    }
}
