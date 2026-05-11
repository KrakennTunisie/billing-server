package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.enums.OperationCategory;
import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceItemCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceItemDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceItemEntity;
import org.springframework.stereotype.Component;

@Component
public class InvoiceItemMapper {



    public InvoiceItem invoiceItemCreateDTOtoDomain(InvoiceItemCreateDTO dto, Double appliedExchangeRate) {
        if (dto == null) {
            return null;
        }

        Double totalExclTax =( dto.getQuantity() * dto.getUnityPriceEXclTax())/appliedExchangeRate;
        Double taxAmount = totalExclTax * dto.getVatRate() / 100;
        Double totalInclTax = (totalExclTax + taxAmount) ;

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
        double totalExclTax = entity.getTotalPriceIncTax()/(1+(entity.getVatRate() / 100));
        totalExclTax = Math.round(totalExclTax * 100.0) / 100.0;
        double taxAmount = entity.getTotalPriceIncTax() - totalExclTax;
        taxAmount = Math.round(taxAmount * 100.0) / 100.0;

        return InvoiceItem.builder()
                .idInvoiceItem(entity.getIdInvoiceItem())
                .description(entity.getDescription())
                .quantity(entity.getQuantity())
                .unityPriceEXclTax(entity.getUnityPriceEXclTax())
                .vatRate(entity.getVatRate())
                .operationCategory(entity.getOperationCategory())
                .itemTotalExclTax(totalExclTax)
                .itemTaxAmount(taxAmount)
                .itemTotalInclTax(entity.getTotalPriceIncTax())
                .build();
    }

    public InvoiceItem toInvoiceItem(InvoiceItemEntity entity) {
        if (entity == null) {
            return null;
        }

        double totalExclTax = entity.getTotalPriceIncTax()/(1+(entity.getVatRate() / 100));
        totalExclTax = Math.round(totalExclTax * 100.0) / 100.0;
        double taxAmount = entity.getTotalPriceIncTax() - totalExclTax;
        taxAmount = Math.round(taxAmount * 100.0) / 100.0;


        InvoiceItem dto =InvoiceItem.builder()
                .idInvoiceItem(entity.getIdInvoiceItem())
                .description(entity.getDescription())
                .quantity(entity.getQuantity())
                .unityPriceEXclTax(entity.getUnityPriceEXclTax())
                .vatRate(entity.getVatRate())
                .itemTotalExclTax(totalExclTax)
                .itemTaxAmount(taxAmount)
                .itemTotalInclTax(entity.getTotalPriceIncTax())
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
        entity.setOperationCategory(invoiceItem.getOperationCategory());
        entity.setTotalPriceIncTax(invoiceItem.getItemTotalInclTax());

        return entity;
    }
}
