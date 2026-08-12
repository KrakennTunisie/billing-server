package com.example.billingservice.shared;

import com.example.billingservice.application.Utils.StatusMapper;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.infrastructure.out.messaging.NotificationEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class NotificationEventFactory {

    public NotificationEvent createInvoiceCreated(
            UUID invoiceId,
            String invoiceReference,
            BigDecimal amount,
            String currency
    ) {
        return new NotificationEvent(
                UUID.randomUUID().toString(),
                "INVOICE_CREATED",
                "INVOICE",
                List.of(),
                List.of("Admin", "ACCOUNTANT"),
                "Nouvelle facture créée",
                "La facture %s d'un montant de %s %s a été créée."
                        .formatted(invoiceReference, amount, currency),
                Map.of(
                        "invoiceId", invoiceId.toString(),
                        "invoiceReference", invoiceReference,
                        "amount", amount,
                        "currency", currency
                ),
                Instant.now()
        );
    }

    public NotificationEvent createInvoiceStatusUpdated(
            UUID invoiceId,
            String invoiceReference,
            InvoiceStatus invoiceStatus
    ) {
        return new NotificationEvent(
                UUID.randomUUID().toString(),
                "INVOICE_STATUS_UPDATED",
                "INVOICE",
                List.of(),
                List.of("Admin", "ACCOUNTANT"),
                "Statut de la facture mis à jour",
                "Le statut de la facture %s est maintenant : %s."
                        .formatted(invoiceReference, StatusMapper.mapInvoiceStatusToFrench(invoiceStatus)),
                Map.of(
                        "invoiceId", invoiceId.toString(),
                        "invoiceReference", invoiceReference,
                        "invoiceStatus", StatusMapper.mapInvoiceStatusToFrench(invoiceStatus)
                ),
                Instant.now()
        );
    }

    public NotificationEvent createPaymentStatusUpdated(
            UUID invoiceId,
            String invoiceReference,
            String paymentStatus
    ) {
        return new NotificationEvent(
                UUID.randomUUID().toString(),
                "PAYMENT_STATUS_UPDATED",
                "PAYMENT",
                List.of(),
                List.of("ADMIN", "ACCOUNTANT"),
                "Statut de paiement mis à jour",
                "Le statut de paiement de la facture %s est maintenant : %s."
                        .formatted(invoiceReference, paymentStatus),
                Map.of(
                        "invoiceId", invoiceId.toString(),
                        "invoiceReference", invoiceReference,
                        "paymentStatus", paymentStatus
                ),
                Instant.now()
        );
    }
    public NotificationEvent createPaymentCreated(
            UUID paymentId,
            UUID invoiceId,
            String invoiceReference,
            BigDecimal amount,
            String currency
    ) {
        return new NotificationEvent(
                UUID.randomUUID().toString(),
                "PAYMENT_CREATED",
                "PAYMENT",
                List.of(),
                List.of("Admin", "ACCOUNTANT"),
                "Nouveau paiement créé",
                "Un paiement de %s %s pour la facture %s a été créé."
                        .formatted(
                                amount,
                                currency,
                                invoiceReference
                        ),
                Map.of(
                        "paymentId", paymentId.toString(),
                        "invoiceId", invoiceId.toString(),
                        "invoiceReference", invoiceReference,
                        "amount", amount,
                        "currency", currency
                ),
                Instant.now()
        );
    }

}
