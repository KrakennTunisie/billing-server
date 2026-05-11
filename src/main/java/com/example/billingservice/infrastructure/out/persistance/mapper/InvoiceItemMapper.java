package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.application.ports.out.PurchaseOrderItemRepositoryPort;
import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.enums.OperationCategory;
import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.domain.model.PurchaseOrderItem;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceItemCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceItemDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceItemEntity;
import com.example.billingservice.infrastructure.out.persistance.repository.PurchaseOrderItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InvoiceItemMapper {

    private PurchaseOrderItemRepositoryPort purchaseOrderItemPort;
    private PurchaseOrderItemMapper purchaseOrderMapper;

    public InvoiceItem invoiceItemCreateDTOtoDomain(InvoiceItemCreateDTO dto, Double appliedExchangeRate) {
        if (dto == null) {
            return null;
        }
        PurchaseOrderItem purchaseOrderItem =null ;
        Double totalExclTax = dto.getQuantity() * dto.getUnityPriceEXclTax();
        Double taxAmount = totalExclTax * dto.getVatRate() / 100;
        Double totalInclTax = (totalExclTax + taxAmount) / appliedExchangeRate;
        if (dto.getIdPurchaseOrderItem()!=null) {
            purchaseOrderItem = purchaseOrderItemPort.getById(dto.getIdPurchaseOrderItem());
        }
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
                        .purchaseOrderItem(purchaseOrderItem)
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
        PurchaseOrderItem purchaseOrderItem =null ;
        Double totalExclTax = entity.getQuantity() * entity.getUnityPriceEXclTax();
        Double taxAmount = totalExclTax * entity.getVatRate() / 100;
        Double totalInclTax = totalExclTax + taxAmount;
        if (entity.getPurchaseOrderItem()!=null) {
            purchaseOrderItem = purchaseOrderItemPort.getById(entity.getPurchaseOrderItem().getIdPurchaseOrderItem());
        }
        return InvoiceItem.builder()
                .idInvoiceItem(entity.getIdInvoiceItem())
                .description(entity.getDescription())
                .quantity(entity.getQuantity())
                .unityPriceEXclTax(entity.getUnityPriceEXclTax())
                .vatRate(entity.getVatRate())
                .operationCategory(entity.getOperationCategory())
                .purchaseOrderItem(purchaseOrderItem)

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
        PurchaseOrderItem purchaseOrderItem =null ;
        Double totalExclTax = entity.getQuantity() * entity.getUnityPriceEXclTax();
        Double taxAmount = totalExclTax * entity.getVatRate() / 100;
        Double totalInclTax = totalExclTax + taxAmount;
        if (entity.getPurchaseOrderItem()!=null) {
            purchaseOrderItem = purchaseOrderItemPort.getById(entity.getPurchaseOrderItem().getIdPurchaseOrderItem());
        }

        InvoiceItem dto =InvoiceItem.builder()
                .idInvoiceItem(entity.getIdInvoiceItem())
                .purchaseOrderItem(purchaseOrderItem)
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
        if(invoiceItem.getPurchaseOrderItem()!=null) {
            entity.setPurchaseOrderItem(purchaseOrderMapper.purchaseOrderItemtoEntity(invoiceItem.getPurchaseOrderItem()));
        }
        entity.setDescription(invoiceItem.getDescription());
        entity.setQuantity(invoiceItem.getQuantity());
        entity.setUnityPriceEXclTax(invoiceItem.getUnityPriceEXclTax());
        entity.setVatRate(invoiceItem.getVatRate());
        entity.setOperationCategory(invoiceItem.getOperationCategory());
        entity.setTotalPriceIncTax(invoiceItem.getItemTotalInclTax());

        return entity;
    }
}
