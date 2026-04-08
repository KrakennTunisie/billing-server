package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.model.InvoiceEvent;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceEventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceEventMapper {


    // =========================
    // ENTITY → DOMAIN
    // =========================
    public InvoiceEvent toDomain(InvoiceEventEntity entity) {
        if (entity == null) {
            return null;
        }

        InvoiceEvent invoiceEvent = InvoiceEvent.builder()
                .idInvoiceEvent(entity.getIdInvoiceEvent())
                .invoiceEventType(entity.getInvoiceEventType())
                .eventDate(entity.getEventDate())
                .description(entity.getDescription())
                .eventTrigger(entity.getEventTrigger())
                .triggeredBy(entity.getTriggeredBy())
                .build();

        return invoiceEvent;
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public InvoiceEventEntity toEntity(InvoiceEvent domain) {
        if (domain == null) {
            return null;
        }

        InvoiceEventEntity entity = new InvoiceEventEntity();
        entity.setIdInvoiceEvent(domain.getIdInvoiceEvent());
        entity.setInvoiceEventType(domain.getInvoiceEventType());
        entity.setEventDate(domain.getEventDate());
        entity.setDescription(domain.getDescription());
        entity.setEventTrigger(domain.getEventTrigger());
        entity.setTriggeredBy(domain.getTriggeredBy());

        return entity;
    }
}
