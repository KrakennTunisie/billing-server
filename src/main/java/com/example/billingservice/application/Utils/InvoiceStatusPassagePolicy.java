package com.example.billingservice.application.Utils;

import com.example.billingservice.domain.enums.InvoiceStatus;

import java.util.Map;
import java.util.Set;

public final class InvoiceStatusPassagePolicy {
    private static final Map<InvoiceStatus, Set<InvoiceStatus>> ALLOWED_TRANSITIONS = Map.of(
            InvoiceStatus.DRAFT, Set.of(InvoiceStatus.TO_PAY, InvoiceStatus.TO_COLLECT, InvoiceStatus.CANCELLED),
            InvoiceStatus.TO_PAY, Set.of(InvoiceStatus.PAID, InvoiceStatus.CANCELLED),
            InvoiceStatus.TO_COLLECT, Set.of(InvoiceStatus.PAID, InvoiceStatus.CANCELLED),
            InvoiceStatus.PAID, Set.of(),
            InvoiceStatus.CANCELLED, Set.of()
    );

    public static boolean checkTransition(InvoiceStatus current, InvoiceStatus target) {
        Set<InvoiceStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(current, Set.of());

        if (!allowed.contains(target)) {
            throw new IllegalStateException(
                    String.format("Transition not allowed: %s → %s", current, target)
            );
        }
        return false;
    }
}
