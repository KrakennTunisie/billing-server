package com.example.billingservice.application.Utils;

import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.infrastructure.out.persistance.dto.CurrencyTotals;
import com.example.billingservice.shared.CurrencyCalculator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SyncInvoiceItems {

    public static void syncInvoiceItems(Invoice invoice, List<InvoiceItem> itemDTOs) {

        // 1. sécuriser la liste
        List<InvoiceItem> incomingItems =
                itemDTOs != null ? itemDTOs : List.of();

        // 2. index des items existants
        Map<UUID, InvoiceItem> existingById = invoice.getInvoiceItems().stream()
                .filter(i -> i.getIdInvoiceItem() != null)
                .collect(Collectors.toMap(
                        InvoiceItem::getIdInvoiceItem,
                        Function.identity()
                ));

        // 3. ids reçus
        Set<UUID> incomingIds = incomingItems.stream()
                .map(InvoiceItem::getIdInvoiceItem)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 4. suppression des items absents
        List<InvoiceItem> toRemove = invoice.getInvoiceItems().stream()
                .filter(i -> i.getIdInvoiceItem() != null)
                .filter(i -> !incomingIds.contains(i.getIdInvoiceItem()))
                .toList();

        for (InvoiceItem item : toRemove) {
            invoice.removeItem(item);
        }

        // 5. add / update
        for (InvoiceItem dto : incomingItems) {

            if (dto.getIdInvoiceItem() == null) {
                // ➕ NEW ITEM
                InvoiceItem newItem = new InvoiceItem();
                mapItem(dto, newItem);
                invoice.addItem(newItem);

            } else {
                // 🔄 UPDATE ITEM
                InvoiceItem existing = existingById.get(dto.getIdInvoiceItem());

                if (existing == null) {
                    throw new IllegalArgumentException(
                            "InvoiceItem not found in this invoice: " + dto.getIdInvoiceItem()
                    );
                }

                mapItem(dto, existing);
            }
        }

        // 6. 🔢 CALCUL DES TOTAUX
        List<InvoiceItem> items = invoice.getInvoiceItems();

        double totalExclTax = items.stream()
                .mapToDouble(item -> item.getItemTotalExclTax() != null
                        ? item.getItemTotalExclTax()
                        : 0.0)
                .sum();

        double totalInclTax = items.stream()
                .mapToDouble(item -> item.getItemTotalInclTax() != null
                        ? item.getItemTotalInclTax()
                        : 0.0)
                .sum();

        // 7. 💱 Conversion devise
        CurrencyTotals totals = CurrencyCalculator.calculateTotals(
                String.valueOf(invoice.getInvoiceCurrency()),
                totalExclTax,
                totalInclTax,
                invoice.getAppliedExchangeRate()
        );

        // 8. 💾 set totals
        invoice.setTotalExclTaxEUR(totals.totalExclTaxEUR());
        invoice.setTotalInclTaxEUR(totals.totalInclTaxEUR());
        invoice.setTotalExclTaxTND(totals.totalExclTaxTND());
        invoice.setTotalInclTaxTND(totals.totalInclTaxTND()); // ⚠️ correction ici
    }

    private static void mapItem(InvoiceItem source, InvoiceItem target) {

        target.setDescription(source.getDescription());
        target.setQuantity(source.getQuantity());
        target.setUnityPriceEXclTax(source.getUnityPriceEXclTax());
        target.setVatRate(source.getVatRate());

        // recalcul des totaux ligne
        double qty = source.getQuantity() != null ? source.getQuantity() : 0.0;
        double price = source.getUnityPriceEXclTax() != null ? source.getUnityPriceEXclTax() : 0.0;
        double vat = source.getVatRate() != null ? source.getVatRate() : 0.0;

        double totalExcl = qty * price;
        double totalIncl = totalExcl * (1 + vat / 100);

        target.setUnityPriceEXclTax(totalExcl);
        target.setUnityPriceEXclTax(totalIncl);
    }
}
