package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.model.InvoiceCreditNoteEvent;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCreditNoteEventEntity;
import org.springframework.stereotype.Component;

@Component
public class InvoiceCreditNoteEventMapper {

    public InvoiceCreditNoteEvent toDomain(InvoiceCreditNoteEventEntity entity) {
        if (entity == null) {
            return null;
        }

        return InvoiceCreditNoteEvent.builder()
                .idInvoiceEvent(entity.getIdInvoiceEvent())
                .invoiceCreditNoteEventType(entity.getEventType())
                .eventDate(entity.getEventDate())
                .description(entity.getDescription())
                .eventTrigger(entity.getEventTrigger())
                .triggeredBy(entity.getTriggeredBy())
                .build();
    }


    public InvoiceCreditNoteEventEntity toEntity(InvoiceCreditNoteEvent domain) {
        if (domain == null) {
            return null;
        }

        InvoiceCreditNoteEventEntity entity = new InvoiceCreditNoteEventEntity();
        entity.setIdInvoiceEvent(domain.getIdInvoiceEvent());
        entity.setEventType(domain.getInvoiceCreditNoteEventType());
        entity.setEventDate(domain.getEventDate());
        entity.setDescription(domain.getDescription());
        entity.setEventTrigger(domain.getEventTrigger());
        entity.setTriggeredBy(domain.getTriggeredBy());

        return entity;
    }

}
