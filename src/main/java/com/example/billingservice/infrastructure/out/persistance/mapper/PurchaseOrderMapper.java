package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.model.PurchaseOrder;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderSummaryDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.PurchaseOrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PurchaseOrderMapper {


    // =========================
    // ENTITY -> DOMAIN
    // =========================
    public PurchaseOrder toDomain(PurchaseOrderEntity entity) {
        if (entity == null) {
            return null;
        }

        return PurchaseOrder.builder()
                .idPurchaseOrder(entity.getIdPurchaseOrder())
                .reference(entity.getReference())
                .orderDate(entity.getOrderDate())
                .totalAmountExclTax(entity.getTotalAmountExclTax())
                .totalAmountInclTax(entity.getTotalAmountInclTax())
                .build();
    }

    // =========================
    // DOMAIN -> ENTITY
    // =========================
    public PurchaseOrderEntity toEntity(PurchaseOrder domain) {
        if (domain == null) {
            return null;
        }

        PurchaseOrderEntity entity = new PurchaseOrderEntity();

        entity.setIdPurchaseOrder(domain.getIdPurchaseOrder());
        entity.setReference(domain.getReference());
        entity.setOrderDate(domain.getOrderDate());
        entity.setTotalAmountExclTax(domain.getTotalAmountExclTax());
        entity.setTotalAmountInclTax(domain.getTotalAmountInclTax());


        return entity;
    }


    public PurchaseOrderEntity purchaseOrderCreateDTOtoEntity(PurchaseOrderCreateDTO purchaseOrderCreateDTO){
        if (purchaseOrderCreateDTO == null) {
            return null;
        }

        PurchaseOrderEntity entity = new PurchaseOrderEntity();

        entity.setReference(purchaseOrderCreateDTO.getReference());
        entity.setOrderDate(purchaseOrderCreateDTO.getOrderDate());
        entity.setTotalAmountExclTax(purchaseOrderCreateDTO.getTotalAmountExclTax());
        entity.setTotalAmountInclTax(purchaseOrderCreateDTO.getTotalAmountInclTax());



        return entity;
    }

    public PurchaseOrderSummaryDTO toSummaryDTO(PurchaseOrder po) {
        if (po == null) {
            return null;
        }

        return PurchaseOrderSummaryDTO.builder()
                .idPurchaseOrder(po.getIdPurchaseOrder())
                .reference(po.getReference())
                .orderDate(po.getOrderDate())
                .build();
    }

}
