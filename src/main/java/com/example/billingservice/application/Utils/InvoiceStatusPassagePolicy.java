package com.example.billingservice.application.Utils;

import com.example.billingservice.domain.enums.InvoiceStatus;

import java.util.Map;
import java.util.Set;

public final class InvoiceStatusPassagePolicy {
    private static final Map<InvoiceStatus, Set<InvoiceStatus>> ALLOWED_TRANSITIONS = Map.of(

            InvoiceStatus.DRAFT, Set.of(InvoiceStatus.DRAFT,InvoiceStatus.TO_PAY,
                    InvoiceStatus.TO_COLLECT, InvoiceStatus.CANCELLED, InvoiceStatus.PARTIALLY_PAID, InvoiceStatus.PAID),

            InvoiceStatus.TO_PAY, Set.of(InvoiceStatus.TO_PAY,InvoiceStatus.PARTIALLY_PAID,
                    InvoiceStatus.PAID, InvoiceStatus.CANCELLED, InvoiceStatus.OVERDUE),

            InvoiceStatus.TO_COLLECT, Set.of(InvoiceStatus.PARTIALLY_PAID, InvoiceStatus.TO_COLLECT,
                    InvoiceStatus.PAID, InvoiceStatus.CANCELLED, InvoiceStatus.OVERDUE),

            InvoiceStatus.PARTIALLY_PAID, Set.of(InvoiceStatus.PARTIALLY_PAID, InvoiceStatus.PAID,
                    InvoiceStatus.CANCELLED, InvoiceStatus.OVERDUE, InvoiceStatus.TO_COLLECT),

            InvoiceStatus.PAID, Set.of(InvoiceStatus.PARTIALLY_PAID, InvoiceStatus.PAID, InvoiceStatus.TO_COLLECT),

            InvoiceStatus.OVERDUE, Set.of(InvoiceStatus.PARTIALLY_PAID, InvoiceStatus.PAID, InvoiceStatus.CANCELLED),

            InvoiceStatus.CANCELLED, Set.of()
    );

    public static boolean checkTransition(InvoiceStatus current, InvoiceStatus target) {
        Set<InvoiceStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(current, Set.of());

        if (!allowed.contains(target)) {
            throw new IllegalStateException(
                    String.format("Transition impossible: %s → %s",
                            StatusMapper.mapInvoiceStatusToFrench(current)
                            , StatusMapper.mapInvoiceStatusToFrench(target))
            );
        }
        return false;
    }
}
