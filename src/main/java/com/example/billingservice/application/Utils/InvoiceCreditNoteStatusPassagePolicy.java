package com.example.billingservice.application.Utils;

import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;

import java.util.Map;
import java.util.Set;

public final class InvoiceCreditNoteStatusPassagePolicy {
    private static final Map<InvoiceCreditNoteStatus, Set<InvoiceCreditNoteStatus>> ALLOWED_TRANSITIONS = Map.of(
            InvoiceCreditNoteStatus.DRAFT, Set.of(InvoiceCreditNoteStatus.PENDING, InvoiceCreditNoteStatus.CANCELLED),
            InvoiceCreditNoteStatus.PENDING, Set.of(InvoiceCreditNoteStatus.UNFUNDED,InvoiceCreditNoteStatus.REFUNDED, InvoiceCreditNoteStatus.CANCELLED),
            InvoiceCreditNoteStatus.UNFUNDED, Set.of(),
            InvoiceCreditNoteStatus.REFUNDED, Set.of(),
            InvoiceCreditNoteStatus.CANCELLED, Set.of()
    );

    public static void checkTransition(InvoiceCreditNoteStatus current, InvoiceCreditNoteStatus target) {

        if (current == null) {
            throw new IllegalArgumentException("Current invoice credit note status must not be null");
        }

        if (target == null) {
            throw new IllegalArgumentException("Target invoice credit note status must not be null");
        }

        Set<InvoiceCreditNoteStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(current, Set.of());

        if (!allowed.contains(target)) {
            throw new IllegalStateException(
                    String.format("Transition not allowed: %s → %s", current, target)
            );
        }
    }
}
