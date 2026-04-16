package com.example.billingservice.shared;

import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.infrastructure.out.persistance.dto.CurrencyTotals;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyCalculator {

    private static final int DIVISION_SCALE = 6;
    private static final int MONEY_SCALE = 3;

    private CurrencyCalculator() {
    }

    public static CurrencyTotals calculateTotals(
            String currency,
            Double totalExclTax,
            Double totalInclTax,
            Double exchangeRate
    ) {
        validateInputs(currency, totalExclTax, totalInclTax, exchangeRate);

        BigDecimal excl = BigDecimal.valueOf(totalExclTax);
        BigDecimal incl = BigDecimal.valueOf(totalInclTax);
        BigDecimal rate = BigDecimal.valueOf(exchangeRate);

        boolean isEUR = InvoiceCurrency.EUR.name().equalsIgnoreCase(currency);
        boolean isTND = InvoiceCurrency.TND.name().equalsIgnoreCase(currency);

        if (!isEUR && !isTND) {
            throw new IllegalArgumentException("Devise inconnue: " + currency);
        }

        BigDecimal totalExclTaxEUR;
        BigDecimal totalInclTaxEUR;
        BigDecimal totalExclTaxTND;
        BigDecimal totalInclTaxTND;

        if (isEUR) {
            totalExclTaxEUR = excl;
            totalInclTaxEUR = incl;
            totalExclTaxTND = excl.multiply(rate);
            totalInclTaxTND = incl.multiply(rate);
        } else {
            totalExclTaxTND = excl;
            totalInclTaxTND = incl;
            totalExclTaxEUR = excl.divide(rate, DIVISION_SCALE, RoundingMode.HALF_UP);
            totalInclTaxEUR = incl.divide(rate, DIVISION_SCALE, RoundingMode.HALF_UP);
        }

        return new CurrencyTotals(
                scaleMoney(totalExclTaxEUR).doubleValue(),
                scaleMoney(totalInclTaxEUR).doubleValue(),
                scaleMoney(totalExclTaxTND).doubleValue(),
                scaleMoney(totalInclTaxTND).doubleValue()
        );
    }

    private static void validateInputs(
            String currency,
            Double totalExclTax,
            Double totalInclTax,
            Double exchangeRate
    ) {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }

        if (totalExclTax == null) {
            throw new IllegalArgumentException("Total excl tax is required");
        }

        if (totalInclTax == null) {
            throw new IllegalArgumentException("Total incl tax is required");
        }

        if (exchangeRate == null) {
            throw new IllegalArgumentException("Exchange rate is required");
        }

        if (exchangeRate <= 0) {
            throw new IllegalArgumentException("Exchange rate must be greater than 0");
        }
    }

    private static BigDecimal scaleMoney(BigDecimal value) {
        return value.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    public static Double getTotalExclTaxEUR(Invoice invoice) {
        if (invoice.getCurrency() == InvoiceCurrency.EUR) {
            return invoice.getTotalInclTaxEUR();
        }
        if (invoice.getCurrency() == InvoiceCurrency.TND && invoice.getAppliedExchangeRate() != null) {
            return invoice.getTotalExclTaxEUR() / invoice.getAppliedExchangeRate();
        }
        return 0.0;
    }

    public static Double getTotalInclTaxEUR(Invoice invoice) {
        if (invoice.getCurrency() == InvoiceCurrency.EUR) {
            return invoice.getTotalInclTaxEUR();
        }
        if (invoice.getCurrency() == InvoiceCurrency.TND && invoice.getAppliedExchangeRate() != null) {
            return invoice.getTotalInclTaxEUR() / invoice.getAppliedExchangeRate();
        }
        return 0.0;
    }

    public static Double getTotalExclTaxTND(Invoice invoice) {
        if (invoice.getCurrency() == InvoiceCurrency.TND) {
            return invoice.getTotalExclTaxTND();
        }
        if (invoice.getCurrency() == InvoiceCurrency.EUR && invoice.getAppliedExchangeRate() != null) {
            return invoice.getTotalExclTaxEUR() * invoice.getAppliedExchangeRate();
        }
        return 0.0;
    }

    public static Double getTotalInclTaxTND(Invoice invoice) {
        if (invoice.getCurrency() == InvoiceCurrency.TND) {
            return invoice.getTotalInclTaxTND();
        }
        if (invoice.getCurrency() == InvoiceCurrency.EUR && invoice.getAppliedExchangeRate() != null) {
            return invoice.getTotalInclTaxEUR() * invoice.getAppliedExchangeRate();
        }
        return 0.0;
    }
}
