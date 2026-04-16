package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.ExchangeRateSource;
import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.enums.PaymentMethod;
import com.example.billingservice.infrastructure.out.persistance.validators.ValidEnum;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class PurchaseOrderCreateDTO {

    //@NotBlank(message = "Le numéro de facture est obligatoire")
    @Setter
    private String purchaseOrderNumber;

    @NotNull(message = "La date d'émission est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date issueDate;


    @NotNull(message = "Le devise est obligatoire")
    @ValidEnum(enumClass = InvoiceCurrency.class, message = "Devise invalide")
    private String purchaseCurrency;


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

    //@Valid
    //@NotEmpty(message = "Au moins une ligne de facture est obligatoire")
    private List<PurchaseOrderItemCreateDTO> purchaseOrderItems;

    // 👉 Document (like rne/patente)
    @NotNull(message = "Le document de bon de commande est obligatoire")
    private MultipartFile purchaseOrderDocument;

}
