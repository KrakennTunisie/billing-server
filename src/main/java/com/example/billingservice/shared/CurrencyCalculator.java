package com.example.billingservice.shared;

import com.example.billingservice.application.ports.in.CurrencyConversionUseCase;
import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.infrastructure.out.persistance.dto.CurrencyTotals;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
@AllArgsConstructor
public class CurrencyCalculator {

    private static final int MONEY_SCALE = 2;
    private final CurrencyConversionUseCase currencyConversionUseCase;


    public CurrencyTotals calculateTotals(
            String currency,
            Double totalExclTax,
            Double totalInclTax,
            Double exchangeRate,
            Date echangeRateDate
    ) {
        validateInputs(currency, totalExclTax, totalInclTax, exchangeRate);

        BigDecimal eurToTndRate = currencyConversionUseCase
                .convert( InvoiceCurrency.TND.name(), InvoiceCurrency.EUR.name(), convertToLocalDate(echangeRateDate)).getQuote();
        BigDecimal usdToTndRate = currencyConversionUseCase
                .convert( InvoiceCurrency.TND.name(), InvoiceCurrency.USD.name(), convertToLocalDate(echangeRateDate)).getQuote();

        BigDecimal totalExclTaxTND = BigDecimal.valueOf(totalExclTax);
        BigDecimal totalInclTaxTND = BigDecimal.valueOf(totalInclTax);
        BigDecimal rate = BigDecimal.valueOf(exchangeRate);

        boolean isEUR = InvoiceCurrency.EUR.name().equalsIgnoreCase(currency);
        boolean isTND = InvoiceCurrency.TND.name().equalsIgnoreCase(currency);
        boolean isUSD = InvoiceCurrency.USD.name().equalsIgnoreCase(currency);

        if (!isEUR && !isTND && !isUSD) {
            throw new IllegalArgumentException("Devise inconnue: " + currency);
        }
        BigDecimal totalExclTaxEUR;
        BigDecimal totalInclTaxEUR;
        BigDecimal totalExclTaxUSD;
        BigDecimal totalInclTaxUSD;

        if (isEUR) {
            totalExclTaxEUR = totalExclTaxTND.multiply(rate);
            totalInclTaxEUR = totalInclTaxTND.multiply(rate);
            totalExclTaxUSD = totalExclTaxTND.multiply(usdToTndRate);
            totalInclTaxUSD = totalInclTaxTND.multiply(usdToTndRate);

        } else if (isUSD) {
            totalExclTaxUSD = totalExclTaxTND.multiply(rate);
            totalInclTaxUSD = totalInclTaxTND.multiply(rate);

            totalExclTaxEUR = totalExclTaxTND.multiply(eurToTndRate);
            totalInclTaxEUR = totalInclTaxTND.multiply(eurToTndRate);
        } else {
            totalExclTaxEUR = totalExclTaxTND.multiply(eurToTndRate);
            totalInclTaxEUR = totalInclTaxTND.multiply(eurToTndRate);
            totalExclTaxUSD = totalExclTaxTND.multiply(usdToTndRate);
            totalInclTaxUSD = totalInclTaxTND.multiply(usdToTndRate);
        }

        return new CurrencyTotals(
                scaleMoney(totalExclTaxEUR).doubleValue(),
                scaleMoney(totalInclTaxEUR).doubleValue(),
                scaleMoney(totalExclTaxTND).doubleValue(),
                scaleMoney(totalInclTaxTND).doubleValue(),
                scaleMoney(totalExclTaxUSD).doubleValue(),
                scaleMoney(totalInclTaxUSD).doubleValue()
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
            return invoice.getTotalExclTaxEUR();
        }
        return invoice.getTotalExclTaxEUR() != null ? invoice.getTotalExclTaxEUR() : 0.0;
    }

    public static Double getTotalInclTaxEUR(Invoice invoice) {
        if (invoice.getCurrency() == InvoiceCurrency.EUR) {
            return invoice.getTotalInclTaxEUR();
        }
        return invoice.getTotalInclTaxEUR() != null ? invoice.getTotalInclTaxEUR() : 0.0;
    }

    public static Double getTotalExclTaxTND(Invoice invoice) {
        if (invoice.getCurrency() == InvoiceCurrency.TND) {
            return invoice.getTotalExclTaxTND();
        }
        return invoice.getTotalExclTaxTND() != null ? invoice.getTotalExclTaxTND() : 0.0;
    }

    public static Double getTotalInclTaxTND(Invoice invoice) {
        if (invoice.getCurrency() == InvoiceCurrency.TND) {
            return invoice.getTotalInclTaxTND();
        }
        return invoice.getTotalInclTaxTND() != null ? invoice.getTotalInclTaxTND() : 0.0;
    }

    public static Double getTotalExclTaxUSD(Invoice invoice) {
        if (invoice.getCurrency() == InvoiceCurrency.USD) {
            return invoice.getTotalExclTaxUSD();
        }
        return invoice.getTotalExclTaxUSD() != null ? invoice.getTotalExclTaxUSD() : 0.0;
    }

    public static Double getTotalInclTaxUSD(Invoice invoice) {
        if (invoice.getCurrency() == InvoiceCurrency.USD) {
            return invoice.getTotalInclTaxUSD();
        }
        return invoice.getTotalInclTaxUSD() != null ? invoice.getTotalInclTaxUSD() : 0.0;
    }

    public LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.of("Europe/Paris"))
                .toLocalDate();
    }
}
