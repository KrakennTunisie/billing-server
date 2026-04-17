package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.application.ports.in.InvoiceItemUseCase;
import com.example.billingservice.domain.model.InvoiceCreditNoteItem;
import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceCreditNoteItemCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCreditNoteItemEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class InvoiceCreditNoteItemMapper {

    private final InvoiceItemMapper invoiceItemMapper;
    private final InvoiceItemUseCase invoiceItemUseCase;
    // =========================
    // ENTITY -> DOMAIN
    // =========================
    public InvoiceCreditNoteItem toDomain(InvoiceCreditNoteItemEntity entity) {
        if (entity == null) {
            return null;
        }

        return InvoiceCreditNoteItem.builder()
                .idInvoiceCreditNoteItem(entity.getIdInvoiceCreditNoteItem())
                .quantity(entity.getQuantity())
                .invoiceItem(invoiceItemMapper.invoiceItemtoDomain(entity.getInvoiceItem()))
                .build();
    }

    // =========================
    // DOMAIN -> ENTITY
    // =========================
    public InvoiceCreditNoteItemEntity toEntity(InvoiceCreditNoteItem domain) {
        if (domain == null) {
            return null;
        }

        InvoiceCreditNoteItemEntity entity = new InvoiceCreditNoteItemEntity();

        entity.setQuantity(domain.getQuantity());

            entity.setInvoiceItem(
                    invoiceItemUseCase.getInvoiceItemEntityById(domain.getInvoiceItem().getIdInvoiceItem())
            );


        return entity;
    }

    // =========================
    // DTO -> DOMAIN
    // =========================
    public InvoiceCreditNoteItem toDomain(InvoiceCreditNoteItemCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        return InvoiceCreditNoteItem.builder()
                .quantity(dto.getQuantity())
                // ⚠️ on ne mappe pas toute la ligne, seulement l'id
                .invoiceItem(invoiceItemUseCase.getById(UUID.fromString(dto.getIdInvoiceItem())))
                .build();
    }
}
