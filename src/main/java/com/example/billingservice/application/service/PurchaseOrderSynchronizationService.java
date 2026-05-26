package com.example.billingservice.application.service;


import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.application.ports.out.ClientInvoicesRepositoryPort;
import com.example.billingservice.application.ports.out.ClientPurchaseOrderPort;
import com.example.billingservice.application.ports.out.InvoiceItemRepositoryPort;
import com.example.billingservice.application.ports.out.PurchaseOrderItemRepositoryPort;
import com.example.billingservice.domain.enums.OperationCategory;
import com.example.billingservice.domain.enums.PurchaseOrderStatus;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.domain.model.PurchaseOrder;
import com.example.billingservice.domain.model.PurchaseOrderItem;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceItemCreateDTO;

import com.example.billingservice.infrastructure.out.persistance.mapper.InvoiceItemMapper;
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
    private final ClientInvoicesRepositoryPort clientInvoicesRepositoryPort;
    private final InvoiceItemMapper invoiceItemMapper;

    @Transactional
    public List<InvoiceItem> synchronize(UUID purchaseOrderId, List<InvoiceItemCreateDTO> itemsToInvoice) {

        PurchaseOrder purchaseOrder = purchaseOrderPort.getDomainePurchaseOrderById(purchaseOrderId);

        // Si aucun item sélectionné → facturer TOUT ce qui reste
        if (itemsToInvoice == null || itemsToInvoice.isEmpty()) {
            itemsToInvoice = buildAllRemainingItems(purchaseOrder);
        }

        List<InvoiceItem> invoiceItems = processItems(itemsToInvoice, purchaseOrder.getAppliedExchangeRate() );

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

    private List<InvoiceItem> processItems(List<InvoiceItemCreateDTO> itemsToInvoice, double exchangeRate) {
        List<InvoiceItem> invoiceItems = new ArrayList<>();

        for (InvoiceItemCreateDTO itemDTO : itemsToInvoice) {

            PurchaseOrderItem poItem = purchaseOrderItemRepository.getById(itemDTO.getIdPurchaseOrderItem());

            validateQuantity(poItem, Double.valueOf(itemDTO.getQuantity()));

            InvoiceItem invoiceItem = invoiceItemMapper.invoiceItemCreateDTOtoDomain(itemDTO, exchangeRate);
            invoiceItem.setPurchaseOrderItem(poItem);

            invoiceItems.add(invoiceItem);
            poItem.setInvoicedQuantity(poItem.getInvoicedQuantity() + itemDTO.getQuantity());
            purchaseOrderItemRepository.updatedInvoicedQuantity(poItem.getIdPurchaseOrderItem(),poItem.getInvoicedQuantity());
        }

        return invoiceItems;
    }

    private void validateQuantity(PurchaseOrderItem poItem, Double requestedQuantity) {
        double remaining = getRemainingQuantity(poItem);
        if (requestedQuantity > poItem.getQuantity()) {
          throw new BillingException(HttpStatus.CONFLICT, "","La quantité desirée a dépassée la quantité originale de produit");
        }
    }

    private void updatePurchaseOrderStatus(PurchaseOrder purchaseOrder) {
        PurchaseOrder freshPurchaseOrder = purchaseOrderPort.getDomainePurchaseOrderById(purchaseOrder.getIdPurchaseOrder());

        List<PurchaseOrderItem> items = freshPurchaseOrder.getPurchaseOrderItems();
        boolean fullyInvoiced = items.stream()
                .allMatch(item -> getRemainingQuantity(item) == 0);

        boolean totalInvoicedQuantity = items.stream()
                .allMatch(item -> item.getInvoicedQuantity() == 0);

        if (totalInvoicedQuantity)
        {
            purchaseOrderPort.updateStatus(purchaseOrder.getIdPurchaseOrder(),PurchaseOrderStatus.IN_DELIVERY);
        }
        else if (fullyInvoiced) {
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
        int differenceQuantity=0;
        for (InvoiceItemCreateDTO itemDTO : itemsToInvoice) {

            PurchaseOrderItem poItem = purchaseOrderItemRepository.getById(itemDTO.getIdPurchaseOrderItem());
            InvoiceItem invoiceItem = invoiceItemRepositoryPort.getById(itemDTO.getIdInvoiceItem());

            validateQuantity(poItem, Double.valueOf(itemDTO.getQuantity()));

            if(itemDTO.getQuantity() > invoiceItem.getQuantity()) {

                differenceQuantity = Math.abs(invoiceItem.getQuantity() - itemDTO.getQuantity());
                poItem.setInvoicedQuantity(poItem.getInvoicedQuantity() + differenceQuantity);
            }
            if(itemDTO.getQuantity()< invoiceItem.getQuantity())
            {
                differenceQuantity = Math.abs(invoiceItem.getQuantity() - itemDTO.getQuantity());
                poItem.setInvoicedQuantity(poItem.getInvoicedQuantity() - differenceQuantity);
            }

            purchaseOrderItemRepository.updatedInvoicedQuantity(poItem.getIdPurchaseOrderItem(),poItem.getInvoicedQuantity());
            PurchaseOrder purchaseOrder = purchaseOrderPort.getDomainePurchaseOrderById(purchaseOrderId);
            updatePurchaseOrderStatus(purchaseOrder);
        }
    }

    public void deleteInvoiceRelatedToPurchaseOrder (UUID invoiceID)
    {
        Invoice invoice = clientInvoicesRepositoryPort.getInvoice(invoiceID);

        if(clientInvoicesRepositoryPort.existsByPurchaseOrderId(invoice.getPurchaseOrder().getIdPurchaseOrder()))
        {
           List <PurchaseOrderItem> poItems = invoice.getPurchaseOrder().getPurchaseOrderItems();
           List<InvoiceItem> invoiceItems = invoice.getInvoiceItems();
            for (int i = 0; i < Math.min(poItems.size(), invoiceItems.size()); i++) {
                PurchaseOrderItem poItem = poItems.get(i);
                InvoiceItem invoiceItem = invoiceItems.get(i);
                poItem.setInvoicedQuantity(poItem.getInvoicedQuantity() - invoiceItem.getQuantity());
                purchaseOrderItemRepository.updatedInvoicedQuantity(poItem.getIdPurchaseOrderItem(),poItem.getInvoicedQuantity());

            }
            updatePurchaseOrderStatus(invoice.getPurchaseOrder());
            }

        }
    }

