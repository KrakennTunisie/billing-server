package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.DiscountType;
import com.example.billingservice.domain.enums.OperationCategory;
import com.example.billingservice.domain.model.PurchaseOrderItem;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderItemCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.PurchaseOrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderItemMapper {

    public PurchaseOrderItem purchaseOrderItemCreateDTOtoDomain(PurchaseOrderItemCreateDTO dto, Double appliedExchangeRate) {
        if (dto == null) {
            return null;
        }

        double subtotal = (dto.getQuantity() * dto.getUnityPriceEXclTax()) / appliedExchangeRate;

        double discount =
                DiscountType.valueOf(dto.getDiscountType()) == DiscountType.PERCENTAGE
                        ? subtotal * dto.getDiscountValue() / 100
                        : dto.getDiscountValue() / appliedExchangeRate;

        double totalExclTax = Math.max(0, subtotal - discount);

        double taxAmount = totalExclTax * dto.getVatRate() / 100;

        double totalInclTax = totalExclTax + taxAmount;

        return PurchaseOrderItem.builder()
                .description(dto.getDescription())
                .quantity(dto.getQuantity())
                .invoicedQuantity(0)
                .unityPriceEXclTax(dto.getUnityPriceEXclTax())
                .vatRate(dto.getVatRate())
                .itemTotalExclTax(totalExclTax)
                .itemTaxAmount(taxAmount)
                .itemTotalInclTax(totalInclTax)
                .discountType(DiscountType.valueOf(dto.getDiscountType()))
                .discountValue(dto.getDiscountValue())
                .operationCategory(dto.getOperationCategory())

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
        purchaseOrderItemEntity.setInvoicedQuantity(dto.getInvoicedQuantity());
        purchaseOrderItemEntity.setUnityPriceEXclTax(dto.getUnityPriceEXclTax());
        purchaseOrderItemEntity.setTotalPriceIncTax(dto.getItemTotalInclTax());
        purchaseOrderItemEntity.setVatRate(dto.getVatRate());
        purchaseOrderItemEntity.setOperationCategory(dto.getOperationCategory());
        purchaseOrderItemEntity.setDiscountType(dto.getDiscountType());
        purchaseOrderItemEntity.setDiscountValue(dto.getDiscountValue());

        return purchaseOrderItemEntity;
    }


    public PurchaseOrderItem toDomain(PurchaseOrderItemEntity entity) {
        if (entity == null) {
            return null;
        }
        double totalExclTax = entity.getTotalPriceIncTax()/(1+(entity.getVatRate() / 100));
        totalExclTax = Math.round(totalExclTax * 100.0) / 100.0;
        double taxAmount = entity.getTotalPriceIncTax() - totalExclTax;
        taxAmount = Math.round(taxAmount * 100.0) / 100.0;

        return PurchaseOrderItem.builder()
                .idPurchaseOrderItem(entity.getIdPurchaseOrderItem())
                .description(entity.getDescription())
                .quantity(entity.getQuantity())
                .invoicedQuantity(entity.getInvoicedQuantity())
                .unityPriceEXclTax(entity.getUnityPriceEXclTax())
                .vatRate(entity.getVatRate())
                .discountType(entity.getDiscountType())
                .discountValue(entity.getDiscountValue())
                .operationCategory(entity.getOperationCategory())
                .itemTotalExclTax(totalExclTax)
                .itemTaxAmount(taxAmount)
                .itemTotalInclTax(entity.getTotalPriceIncTax())
                .build();
    }

}
