package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseCommercialDocument {

    private String reference;

    private Date issueDate;
    private InvoiceCurrency currency;
    private PaymentMethod paymentMethod;
    private Double totalExclTaxEUR;
    private Double totalInclTaxEUR;
    private Double totalExclTaxTND;
    private Double totalInclTaxTND;
    private Double vatRate;
    private Date exchangeRateReferenceDate;
    private Double appliedExchangeRate;
    private ExchangeRateSource exchangeRateSource;
    private Partner partner;
}
