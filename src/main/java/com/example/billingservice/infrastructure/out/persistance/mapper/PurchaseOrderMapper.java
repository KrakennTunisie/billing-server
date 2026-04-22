package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.domain.enums.*;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.domain.model.PurchaseOrder;
import com.example.billingservice.domain.model.PurchaseOrderItem;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.infrastructure.out.persistance.entity.PurchaseOrderEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.PurchaseOrderItemEntity;
import com.example.billingservice.shared.CurrencyCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PurchaseOrderMapper {

    private final PurchaseOrderItemMapper purchaseOrderItemMapper;
    private final PartnerMapper partnerMapper;
    private final PartnerUseCase partnerUseCase;
    private final DocumentMapper documentMapper;
    private final CurrencyCalculator currencyCalculator;

    // =========================
    // ENTITY -> DOMAIN
    // =========================
    public PurchaseOrder toDomain(PurchaseOrderEntity entity) {
        if (entity == null) {
            return null;
        }

        List<PurchaseOrderItem> items = entity.getPurchaseOrderItems() != null
                ? entity.getPurchaseOrderItems()
                .stream()
                .map(purchaseOrderItemMapper::toDomain)
                .toList()
                : List.of();

        double totalExclTax = items.stream()
                .mapToDouble(item -> item.getItemTotalExclTax() != null ? item.getItemTotalExclTax() : 0.0)
                .sum();

        double totalInclTax = items.stream()
                .mapToDouble(item -> item.getItemTotalInclTax() != null ? item.getItemTotalInclTax() : 0.0)
                .sum();

        CurrencyTotals totals = currencyCalculator.calculateTotals(
                entity.getCurrency().name(),
                totalExclTax,
                totalInclTax,
                entity.getAppliedExchangeRate()
        );


        return PurchaseOrder.builder()
                .idPurchaseOrder(entity.getIdPurchaseOrder())
                .reference(entity.getReference())
                .issueDate(entity.getIssueDate())
                .currency(entity.getCurrency())
                .paymentMethod(entity.getPaymentMethod())
                .totalExclTaxEUR(totals.totalExclTaxEUR())
                .totalInclTaxEUR(totals.totalInclTaxEUR())
                .totalExclTaxTND(totals.totalExclTaxTND())
                .totalInclTaxTND(totals.totalInclTaxTND())
                .totalExclTaxUSD(totals.totalExclTaxUSD())
                .totalInclTaxUSD(totals.totalInclTaxUSD())
                .vatRate(entity.getVatRate())
                .partner(partnerMapper.toDomain(entity.getPartner(), PartnerType.CLIENT))
                .exchangeRateReferenceDate(entity.getExchangeRateReferenceDate())
                .appliedExchangeRate(entity.getAppliedExchangeRate())
                .exchangeRateSource(entity.getExchangeRateSource())
                .purchaseOrderItems(items)
                .purchaseOrderDocument(documentMapper.toDomain(entity.getPurchaseOrderDocument()))
                .build();
                //.totalAmountExclTax(entity.getTotalAmountExclTax())
               // .totalAmountInclTax(entity.getTotalAmountInclTax())
    }

    // =========================
    // DOMAIN -> ENTITY
    // =========================
    public PurchaseOrderEntity toEntity(PurchaseOrder domain) {
        if (domain == null) {
            return null;
        }
        List<PurchaseOrderItemEntity> items = domain.getPurchaseOrderItems() != null
                ? domain.getPurchaseOrderItems()
                .stream()
                .map(purchaseOrderItemMapper::purchaseOrderItemtoEntity)
                .toList()
                : List.of();


        PurchaseOrderEntity entity = new PurchaseOrderEntity();

        entity.setIdPurchaseOrder(domain.getIdPurchaseOrder());
        entity.setReference(domain.getReference());
        entity.setIssueDate(domain.getIssueDate());
        entity.setCurrency(domain.getCurrency());
        entity.setAppliedExchangeRate(domain.getAppliedExchangeRate());
        entity.setVatRate(domain.getVatRate());
        entity.setExchangeRateSource(domain.getExchangeRateSource());
        entity.setPurchaseOrderDocument(documentMapper.toEntity(domain.getPurchaseOrderDocument(), DocumentType.PURCHASE_ORDER));
        entity.setPaymentMethod(domain.getPaymentMethod());
        entity.setPartner(partnerMapper.toEntity(domain.getPartner()));
        entity.setPurchaseOrderItems(items);
        //entity.setTotalAmountExclTax(domain.getTotalAmountExclTax());
       // entity.setTotalAmountInclTax(domain.getTotalAmountInclTax());


        return entity;
    }


    public PurchaseOrder purchaseOrderCreateDTOtoDomain(PurchaseOrderCreateDTO purchaseOrderCreateDTO, Document document) throws BillingException {
        if (purchaseOrderCreateDTO == null) {
            return null;
        }
        try{
            PurchaseOrder purchaseOrder =  PurchaseOrder.builder()
                    .reference(purchaseOrderCreateDTO.getPurchaseOrderNumber())
                    .issueDate(purchaseOrderCreateDTO.getIssueDate())
                    .currency(InvoiceCurrency.valueOf(purchaseOrderCreateDTO.getPurchaseCurrency()))
                    .vatRate(purchaseOrderCreateDTO.getVatRate())
                    .appliedExchangeRate(purchaseOrderCreateDTO.getAppliedExchangeRate())
                    .purchaseOrderDocument(document)
                    .paymentMethod(PaymentMethod.valueOf(purchaseOrderCreateDTO.getPaymentMethod()))
                    .exchangeRateReferenceDate(purchaseOrderCreateDTO.getExchangeRateReferenceDate())
                    .exchangeRateSource(ExchangeRateSource.valueOf(purchaseOrderCreateDTO.getExchangeRateSource()))
                    .build();

            Partner partner = getPartner(InvoiceType.SALE, purchaseOrderCreateDTO.getPartner());

            purchaseOrder.setPartner(partner);


            List<PurchaseOrderItem> items = purchaseOrderCreateDTO.getPurchaseOrderItems() != null
                    ? purchaseOrderCreateDTO.getPurchaseOrderItems()
                    .stream()
                    .map(purchaseOrderItemMapper::purchaseOrderItemCreateDTOtoDomain)
                    .toList()
                    : List.of();

            purchaseOrder.setPurchaseOrderItems(items);


            double totalExclTax = items.stream()
                    .mapToDouble(item -> item.getItemTotalExclTax() != null ? item.getItemTotalExclTax() : 0.0)
                    .sum();

            double totalInclTax = items.stream()
                    .mapToDouble(item -> item.getItemTotalInclTax() != null ? item.getItemTotalInclTax() : 0.0)
                    .sum();

            CurrencyTotals totals = currencyCalculator.calculateTotals(
                    purchaseOrderCreateDTO.getPurchaseCurrency(),
                    totalExclTax,
                    totalInclTax,
                    purchaseOrder.getAppliedExchangeRate()
            );


            purchaseOrder.setTotalExclTaxEUR(totals.totalExclTaxEUR());
            purchaseOrder.setTotalInclTaxEUR(totals.totalInclTaxEUR());
            purchaseOrder.setTotalExclTaxTND(totals.totalExclTaxTND());
            purchaseOrder.setTotalInclTaxTND(totals.totalExclTaxTND());
            purchaseOrder.setTotalExclTaxUSD(totals.totalExclTaxUSD());
            purchaseOrder.setTotalInclTaxUSD(totals.totalInclTaxUSD());
            return purchaseOrder;
        }
        catch (Exception exception){
            throw BillingException.badRequest(exception.getMessage());
        }

    }

    public PurchaseOrderDTO domainToPurchaseOrderDTO(PurchaseOrder purchaseOrder){
        if (purchaseOrder == null) return null;

        PurchaseOrderDTO purchaseOrderDTO =  PurchaseOrderDTO.builder()
                .idPurchaseOrder(purchaseOrder.getIdPurchaseOrder())
                .purchaseOrderNumber(purchaseOrder.getReference())
                .issueDate(purchaseOrder.getIssueDate())
                .purchaseCurrency(purchaseOrder.getCurrency())
                .totalExclTaxEUR(purchaseOrder.getTotalExclTaxEUR())
                .totalInclTaxEUR(purchaseOrder.getTotalInclTaxEUR())
                .totalExclTaxTND(purchaseOrder.getTotalExclTaxTND())
                .totalInclTaxTND(purchaseOrder.getTotalInclTaxTND())
                .totalExclTaxUSD(purchaseOrder.getTotalExclTaxUSD())
                .totalInclTaxUSD(purchaseOrder.getTotalInclTaxUSD())
                .vatRate(purchaseOrder.getVatRate())
                .paymentMethod(purchaseOrder.getPaymentMethod())
                .partner(partnerMapper.toSummaryDTO(purchaseOrder.getPartner()))
                .purchaseOrderItemsItems(purchaseOrder.getPurchaseOrderItems())
                .purchaseOrderDocument(documentMapper.toDocumentSummary(purchaseOrder.getPurchaseOrderDocument()))
                .build();


        return purchaseOrderDTO;
    }

    public PurchaseOrderPageItemDTO domainToPageItem(PurchaseOrder purchaseOrder){
        if (purchaseOrder == null) {
            return null;
        }

        return PurchaseOrderPageItemDTO.builder()
                .idPurchaseOrder(purchaseOrder.getIdPurchaseOrder())
                .purchaseOrderNumber(purchaseOrder.getReference())
                .issueDate(purchaseOrder.getIssueDate())
                .purchaseCurrency(purchaseOrder.getCurrency())

                // 💰 Currency split
                .totalExclTaxEUR(purchaseOrder.getTotalExclTaxEUR())
                .totalInclTaxEUR(purchaseOrder.getTotalInclTaxEUR())
                .totalExclTaxTND(purchaseOrder.getTotalExclTaxTND())
                .totalInclTaxTND(purchaseOrder.getTotalInclTaxTND())
                .totalExclTaxUSD(purchaseOrder.getTotalExclTaxUSD())
                .totalInclTaxUSD(purchaseOrder.getTotalInclTaxUSD())
                .vatRate(purchaseOrder.getVatRate())
                .appliedExchangeRate(purchaseOrder.getAppliedExchangeRate())

                // ⚠️ Keep light (avoid deep mapping for page/list)
                .partner(partnerMapper.toSummaryDTO(purchaseOrder.getPartner()))

                .build();
    }

    public PurchaseOrder updateDTOtoDomain(PurchaseOrderUpdateDTO purchaseOrderUpdateDTO, PurchaseOrder purchaseOrder, Document document){
        if (purchaseOrderUpdateDTO == null || purchaseOrder == null) {
            return null;
        }

        try {

            PurchaseOrder purchaseOrder1 = PurchaseOrder.builder()
                    .idPurchaseOrder(purchaseOrder.getIdPurchaseOrder())
                    .reference(purchaseOrder.getReference())
                    .issueDate(purchaseOrderUpdateDTO.getIssueDate())
                    .currency(InvoiceCurrency.valueOf(purchaseOrderUpdateDTO.getPurchaseCurrency()))
                    .vatRate(purchaseOrderUpdateDTO.getVatRate())
                    .appliedExchangeRate(purchaseOrderUpdateDTO.getAppliedExchangeRate())
                    .purchaseOrderDocument(document)
                    .paymentMethod(PaymentMethod.valueOf(purchaseOrderUpdateDTO.getPaymentMethod()))
                    .exchangeRateReferenceDate(purchaseOrderUpdateDTO.getExchangeRateReferenceDate())
                    .exchangeRateSource(ExchangeRateSource.valueOf(purchaseOrderUpdateDTO.getExchangeRateSource()))
                    .build();


            String idPartner = String.valueOf(purchaseOrderUpdateDTO.getPartner());
            Partner partner = getPartner(InvoiceType.SALE, idPartner);
            purchaseOrder1.setPartner(partner);



            List<PurchaseOrderItem> items = purchaseOrderUpdateDTO.getPurchaseOrderItems() != null
                    ? purchaseOrderUpdateDTO.getPurchaseOrderItems()
                    .stream()
                    .map(purchaseOrderItemMapper::purchaseOrderItemCreateDTOtoDomain)
                    .toList()
                    : List.of();


            purchaseOrder1.setPurchaseOrderItems(items);

            double totalExclTax = items.stream()
                    .mapToDouble(item -> item.getItemTotalExclTax() != null ? item.getItemTotalExclTax() : 0.0)
                    .sum();

            double totalInclTax = items.stream()
                    .mapToDouble(item -> item.getItemTotalInclTax() != null ? item.getItemTotalInclTax() : 0.0)
                    .sum();

            CurrencyTotals totals = currencyCalculator.calculateTotals(
                    String.valueOf(purchaseOrder1.getCurrency()),
                    totalExclTax,
                    totalInclTax,
                    purchaseOrder1.getAppliedExchangeRate()
            );


            purchaseOrder1.setTotalExclTaxEUR(totals.totalExclTaxEUR());
            purchaseOrder1.setTotalInclTaxEUR(totals.totalInclTaxEUR());
            purchaseOrder1.setTotalExclTaxTND(totals.totalExclTaxTND());
            purchaseOrder1.setTotalInclTaxTND(totals.totalExclTaxTND());
            purchaseOrder1.setTotalExclTaxUSD(totals.totalExclTaxUSD());
            purchaseOrder1.setTotalInclTaxUSD(totals.totalInclTaxUSD());

            return purchaseOrder1;

        } catch (Exception exception) {
            throw BillingException.badRequest(exception.getMessage());
        }
    }

    public PurchaseOrderSummaryDTO toSummaryDTO(PurchaseOrder po) {
        if (po == null) {
            return null;
        }

        return PurchaseOrderSummaryDTO.builder()
                .idPurchaseOrder(po.getIdPurchaseOrder())
                .purchaseOrderNumber(po.getReference())
                .issueDate(po.getIssueDate())
                .purchaseCurrency(po.getCurrency())
                .build();
    }

    public Partner getPartner(InvoiceType invoiceType, String idPartner){
        if(invoiceType == InvoiceType.PURCHASE){
            return partnerUseCase.getSupplierById(idPartner).get();
        }
        if (invoiceType == InvoiceType.SALE){
            return partnerUseCase.findCustomerById(idPartner).get();
        }
        else {
            throw BillingException.notFound("Partner ", idPartner);
        }
    }

}
