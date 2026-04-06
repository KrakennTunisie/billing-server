package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.OperationCategory;
import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceItemCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceItemDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceItemEntity;
import org.springframework.stereotype.Component;

@Component
public class InvoiceItemMapper {



    public InvoiceItem invoiceItemCreateDTOtoDomain(InvoiceItemCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        Double totalExclTax = dto.getQuantity() * dto.getUnityPriceEXclTax();
        Double taxAmount = totalExclTax * dto.getVatRate() / 100;
        Double totalInclTax = totalExclTax + taxAmount;

        InvoiceItem invoiceItem =
                InvoiceItem.builder()
                        .description(dto.getDescription())
                        .quantity(dto.getQuantity())
                        .unityPriceEXclTax(dto.getUnityPriceEXclTax())
                        .vatRate(dto.getVatRate())
                        .itemTotalExclTax(totalExclTax)
                        .itemTaxAmount(taxAmount)
                        .itemTotalInclTax(totalInclTax)
                        .operationCategory(OperationCategory.valueOf(dto.getOperationCategory()))
                        .build();

        return invoiceItem;
    }

    public InvoiceItemEntity invoiceItemDTOtoEntity(InvoiceItemDTO dto) {
        if (dto == null) {
            return null;
        }


        InvoiceItemEntity invoiceItemEntity = new InvoiceItemEntity();
        invoiceItemEntity.setDescription(dto.getDescription());
        invoiceItemEntity.setQuantity(dto.getQuantity());
        invoiceItemEntity.setUnityPriceEXclTax(dto.getUnityPriceEXclTax());
        invoiceItemEntity.setVatRate(dto.getVatRate());
        invoiceItemEntity.setOperationCategory(OperationCategory.valueOf(dto.getOperationCategory()));

        return invoiceItemEntity;
    }


    public InvoiceItem invoiceItemtoDomain(InvoiceItemEntity entity) {
        if (entity == null) {
            return null;
        }
        Double totalExclTax = entity.getQuantity() * entity.getUnityPriceEXclTax();
        Double taxAmount = totalExclTax * entity.getVatRate() / 100;
        Double totalInclTax = totalExclTax + taxAmount;

        return InvoiceItem.builder()
                .idInvoiceItem(entity.getIdInvoiceItem())
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

    public InvoiceItemDTO toInvoiceItemDTO(InvoiceItemEntity entity) {
        if (entity == null) {
            return null;
        }

        Double totalExclTax = entity.getQuantity() * entity.getUnityPriceEXclTax();
        Double taxAmount = totalExclTax * entity.getVatRate() / 100;
        Double totalInclTax = totalExclTax + taxAmount;


            InvoiceItemDTO dto =InvoiceItemDTO.builder()
                    .idInvoiceItem(entity.getIdInvoiceItem())
                    .description(entity.getDescription())
                    .quantity(entity.getQuantity())
                    .unityPriceEXclTax(entity.getUnityPriceEXclTax())
                    .vatRate(entity.getVatRate())
                    .itemTotalExclTax(totalExclTax)
                    .itemTaxAmount(taxAmount)
                    .itemTotalInclTax(totalInclTax)
                    .operationCategory(String.valueOf(entity.getOperationCategory())).build();

        return dto;
    }


    public InvoiceItem toInvoiceItem(InvoiceItemEntity entity) {
        if (entity == null) {
            return null;
        }

        Double totalExclTax = entity.getQuantity() * entity.getUnityPriceEXclTax();
        Double taxAmount = totalExclTax * entity.getVatRate() / 100;
        Double totalInclTax = totalExclTax + taxAmount;


        InvoiceItem dto =InvoiceItem.builder()
                .idInvoiceItem(entity.getIdInvoiceItem())
                .description(entity.getDescription())
                .quantity(entity.getQuantity())
                .unityPriceEXclTax(entity.getUnityPriceEXclTax())
                .vatRate(entity.getVatRate())
                .itemTotalExclTax(totalExclTax)
                .itemTaxAmount(taxAmount)
                .itemTotalInclTax(totalInclTax)
                .operationCategory(entity.getOperationCategory())
                .build();
        return dto;
    }

    public InvoiceItemEntity invoiceItemToInvoiceEntity(InvoiceItem invoiceItem){
        if (invoiceItem == null) {
            return null;
        }

        InvoiceItemEntity entity = new InvoiceItemEntity();

        entity.setIdInvoiceItem(invoiceItem.getIdInvoiceItem());
        entity.setDescription(invoiceItem.getDescription());
        entity.setQuantity(invoiceItem.getQuantity());
        entity.setUnityPriceEXclTax(invoiceItem.getUnityPriceEXclTax());
        entity.setVatRate(invoiceItem.getVatRate());
        entity.setOperationCategory(entity.getOperationCategory());

        return entity;
    }
}
