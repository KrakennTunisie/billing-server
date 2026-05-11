package com.example.billingservice.application.service;


import com.example.billingservice.application.ports.out.ClientPurchaseOrderPort;
import com.example.billingservice.application.ports.out.InvoiceItemRepositoryPort;
import com.example.billingservice.application.ports.out.PurchaseOrderItemRepositoryPort;
import com.example.billingservice.domain.enums.OperationCategory;
import com.example.billingservice.domain.enums.PurchaseOrderStatus;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.domain.model.PurchaseOrder;
import com.example.billingservice.domain.model.PurchaseOrderItem;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceItemCreateDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderSynchronizationService {
    private final ClientPurchaseOrderPort purchaseOrderPort;
    private final PurchaseOrderItemRepositoryPort purchaseOrderItemRepository;
    private final InvoiceItemRepositoryPort  invoiceItemRepositoryPort;

    @Transactional
    public List<InvoiceItem> synchronize(UUID purchaseOrderId, List<InvoiceItemCreateDTO> itemsToInvoice) {

        PurchaseOrder purchaseOrder = purchaseOrderPort.getDomainePurchaseOrderById(purchaseOrderId);

        // Si aucun item sélectionné → facturer TOUT ce qui reste
        if (itemsToInvoice == null || itemsToInvoice.isEmpty()) {
            itemsToInvoice = buildAllRemainingItems(purchaseOrder);
        }

        List<InvoiceItem> invoiceItems = processItems(itemsToInvoice);

        updatePurchaseOrderStatus(purchaseOrder);

        return invoiceItems;
    }

    private List<InvoiceItemCreateDTO> buildAllRemainingItems(PurchaseOrder purchaseOrder) {
        return purchaseOrder.getPurchaseOrderItems().stream()
                .filter(poItem -> getRemainingQuantity(poItem) > 0)
                .map(poItem -> InvoiceItemCreateDTO.builder()
                        .idPurchaseOrderItem(poItem.getIdPurchaseOrderItem())
                        .description(poItem.getDescription())
                        .operationCategory(String.valueOf(poItem.getOperationCategory()))
                        .vatRate(poItem.getVatRate())
                        .itemTotalInclTax(poItem.getItemTotalInclTax())
                        .quantity((int) getRemainingQuantity(poItem))
                        .unityPriceEXclTax(poItem.getUnityPriceEXclTax())
                        .build())
                        .collect(Collectors.toList());
    }

    private List<InvoiceItem> processItems(List<InvoiceItemCreateDTO> itemsToInvoice) {
        List<InvoiceItem> invoiceItems = new ArrayList<>();

        for (InvoiceItemCreateDTO itemDTO : itemsToInvoice) {

            PurchaseOrderItem poItem = purchaseOrderItemRepository.getById(itemDTO.getIdPurchaseOrderItem());

            validateQuantity(poItem, Double.valueOf(itemDTO.getQuantity()));

            InvoiceItem invoiceItem = InvoiceItem.builder()
                    .purchaseOrderItem(poItem)
                    .quantity(itemDTO.getQuantity())
                    .unityPriceEXclTax(itemDTO.getUnityPriceEXclTax())
                    .description(itemDTO.getDescription())
                    .vatRate(itemDTO.getVatRate())
                    .operationCategory(OperationCategory.valueOf(itemDTO.getOperationCategory()))
                    .itemTaxAmount(itemDTO.getItemTaxAmount())
                    .itemTotalExclTax(itemDTO.getItemTotalExclTax())
                    .itemTotalInclTax(itemDTO.getItemTotalInclTax())
                    .build();

            invoiceItems.add(invoiceItem);
            poItem.setInvoicedQuantity(poItem.getInvoicedQuantity() + itemDTO.getQuantity());
            purchaseOrderItemRepository.updatedInvoicedQuantity(poItem.getIdPurchaseOrderItem(),poItem.getInvoicedQuantity());
        }

        return invoiceItems;
    }

    private void validateQuantity(PurchaseOrderItem poItem, Double requestedQuantity) {
        double remaining = getRemainingQuantity(poItem);
        if (requestedQuantity > remaining) {
          throw new BillingException(HttpStatus.CONFLICT, "","La quantité desirée a dépassée la quantité originale de produit");
        }
    }

    private void updatePurchaseOrderStatus(PurchaseOrder purchaseOrder) {
        PurchaseOrder freshPurchaseOrder = purchaseOrderPort.getDomainePurchaseOrderById(purchaseOrder.getIdPurchaseOrder());

        List<PurchaseOrderItem> items = freshPurchaseOrder.getPurchaseOrderItems();
        boolean fullyInvoiced = items.stream()
                .allMatch(item -> getRemainingQuantity(item) == 0);

        if (fullyInvoiced) {
            purchaseOrderPort.updateStatus(purchaseOrder.getIdPurchaseOrder(),PurchaseOrderStatus.FULLY_INVOICED);

        } else  {
            purchaseOrderPort.updateStatus(purchaseOrder.getIdPurchaseOrder(),PurchaseOrderStatus.PARTIALLY_INVOICED);
        }
    }

    private double getRemainingQuantity(PurchaseOrderItem poItem) {
        return poItem.getQuantity() - poItem.getInvoicedQuantity();
    }

    public void updatePurchaseOrderItemInvoicedQuantity (List<InvoiceItemCreateDTO> itemsToInvoice,UUID purchaseOrderId)
    {
        for (InvoiceItemCreateDTO itemDTO : itemsToInvoice) {

            PurchaseOrderItem poItem = purchaseOrderItemRepository.getById(itemDTO.getIdPurchaseOrderItem());
            InvoiceItem invoiceItem = invoiceItemRepositoryPort.getById(itemDTO.getIdInvoiceItem());

            validateQuantity(poItem, Double.valueOf(itemDTO.getQuantity()));

            int differenceQuantity = Math.abs(invoiceItem.getQuantity() - itemDTO.getQuantity());
            
            poItem.setInvoicedQuantity(poItem.getInvoicedQuantity() + differenceQuantity);
            purchaseOrderItemRepository.updatedInvoicedQuantity(poItem.getIdPurchaseOrderItem(),poItem.getInvoicedQuantity());
            PurchaseOrder purchaseOrder = purchaseOrderPort.getDomainePurchaseOrderById(purchaseOrderId);
            updatePurchaseOrderStatus(purchaseOrder);
        }
    }



}
