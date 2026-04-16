package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.*;
import com.example.billingservice.infrastructure.out.persistance.validators.ValidEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class InvoiceCreateDTO {

    //@NotBlank(message = "Le numéro de facture est obligatoire")
    //@Setter
   // private String invoiceNumber;

    @NotNull(message = "La date d'émission est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date issueDate;

    @NotNull(message = "La date d'échéance est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    @NotNull(message = "Le type de facture est obligatoire")
    @ValidEnum(enumClass = InvoiceType.class, message = "Type de facture invalide")
    private String invoiceType;

    @NotNull(message = "Le devise est obligatoire")
    @ValidEnum(enumClass = InvoiceCurrency.class, message = "Devise invalide")
    private String invoiceCurrency;


    @NotNull(message = "Le taux de TVA est obligatoire")
    @DecimalMin(value = "0.0", message = "Le taux de TVA doit être positif")
    @DecimalMax(value = "100.0", message = "Le taux de TVA ne peut pas dépasser 100%")
    private Double vatRate;

    @NotNull(message = "Le mode de paiement est obligatoire")
    @ValidEnum(enumClass = PaymentMethod.class, message = "Mode de paiement invalide")
    private String paymentMethod;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date exchangeRateReferenceDate;

    private Double appliedExchangeRate;

    @ValidEnum(enumClass = ExchangeRateSource.class, message = "Source du taux invalide")
    private String exchangeRateSource;

    @NotNull(message = "Le partenaire est obligatoire")
    private String partner;

    private String purchaseOrder;

    @Setter
    //@NotEmpty(message = "Au moins une ligne de facture est obligatoire")
    private List<InvoiceItemCreateDTO> invoiceItems;

    // 👉 Document (like rne/patente)
    @NotNull(message = "Le document de facture est obligatoire")
    private MultipartFile invoiceDocument;

    @Override
    public String toString() {
        return "InvoiceCreateDTO{" +
               // "invoiceNumber='" + invoiceNumber + '\'' +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", invoiceType='" + invoiceType + '\'' +
                ", invoiceCurrency='" + invoiceCurrency + '\'' +
                ", vatRate=" + vatRate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", exchangeRateReferenceDate=" + exchangeRateReferenceDate +
                ", appliedExchangeRate=" + appliedExchangeRate +
                ", exchangeRateSource='" + exchangeRateSource + '\'' +
                ", partner='" + partner + '\'' +
                ", purchaseOrder='" + purchaseOrder + '\'' +
                ", invoiceItems=" + invoiceItems +
                ", invoiceDocument=" + invoiceDocument +
                '}';
    }
}
