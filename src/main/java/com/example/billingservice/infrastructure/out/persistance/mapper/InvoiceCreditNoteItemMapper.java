package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.model.InvoiceCreditNoteItem;
import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceCreditNoteItemCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCreditNoteItemEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InvoiceCreditNoteItemMapper {

    private final InvoiceItemMapper invoiceItemMapper;

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

        entity.setIdInvoiceCreditNoteItem(domain.getIdInvoiceCreditNoteItem());
        entity.setQuantity(domain.getQuantity());

        // mapping original invoice item
        if (domain.getInvoiceItem() != null) {
            entity.setInvoiceItem(
                    invoiceItemMapper.invoiceItemToInvoiceEntity(domain.getInvoiceItem())
            );
        }


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
                .invoiceItem(
                        InvoiceItem.builder()
                                .idInvoiceItem(dto.getOriginalInvoiceItemId())
                                .build()
                )
                .build();
    }
}
