package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.OperationCategory;
import com.example.billingservice.domain.model.PurchaseOrderItem;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderItemCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.PurchaseOrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderItemMapper {

    public PurchaseOrderItem purchaseOrderItemCreateDTOtoDomain(PurchaseOrderItemCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        Double totalExclTax = dto.getQuantity() * dto.getUnityPriceEXclTax();
        Double taxAmount = totalExclTax * dto.getVatRate() / 100;
        Double totalInclTax = totalExclTax + taxAmount;

        return PurchaseOrderItem.builder()
                .description(dto.getDescription())
                .quantity(dto.getQuantity())
                .unityPriceEXclTax(dto.getUnityPriceEXclTax())
                .vatRate(dto.getVatRate())
                .itemTotalExclTax(totalExclTax)
                .itemTaxAmount(taxAmount)
                .itemTotalInclTax(totalInclTax)
                .operationCategory(OperationCategory.valueOf(dto.getOperationCategory()))
                .build();
    }

    public PurchaseOrderItemEntity purchaseOrderItemtoEntity(PurchaseOrderItem dto) {
        if (dto == null) {
            return null;
        }


        PurchaseOrderItemEntity purchaseOrderItemEntity = new PurchaseOrderItemEntity();
        purchaseOrderItemEntity.setIdPurchaseOrderItem(dto.getIdPurchaseOrderItem());
        purchaseOrderItemEntity.setDescription(dto.getDescription());
        purchaseOrderItemEntity.setQuantity(dto.getQuantity());
        purchaseOrderItemEntity.setUnityPriceEXclTax(dto.getUnityPriceEXclTax());
        purchaseOrderItemEntity.setVatRate(dto.getVatRate());
        purchaseOrderItemEntity.setOperationCategory(dto.getOperationCategory());

        return purchaseOrderItemEntity;
    }


    public PurchaseOrderItem toDomain(PurchaseOrderItemEntity entity) {
        if (entity == null) {
            return null;
        }
        Double totalExclTax = entity.getQuantity() * entity.getUnityPriceEXclTax();
        Double taxAmount = totalExclTax * entity.getVatRate() / 100;
        Double totalInclTax = totalExclTax + taxAmount;

        return PurchaseOrderItem.builder()
                .idPurchaseOrderItem(entity.getIdPurchaseOrderItem())
                .description(entity.getDescription())
                .quantity(entity.getQuantity())
                .unityPriceEXclTax(entity.getUnityPriceEXclTax())
                .vatRate(entity.getVatRate())
                .operationCategory(entity.getOperationCategory())
                .itemTotalExclTax(totalExclTax)
                .itemTaxAmount(taxAmount)
                .itemTotalInclTax(totalInclTax)
                .build();
    }

}
