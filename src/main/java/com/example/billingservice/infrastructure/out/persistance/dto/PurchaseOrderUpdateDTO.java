package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.*;
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
import java.util.UUID;

@Getter
@Builder
public class PurchaseOrderUpdateDTO {

    @NotNull(message = "L'id de Bon de commande est obligatoire")
    private UUID idPurchaseOrder;

    @Setter
    private String purchaseOrderNumber;

    @NotNull(message = "La date d'émission est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date issueDate;

    @NotNull(message = "La status de bon commande est obligatoire")
    private PurchaseOrderStatus purchaseOrderStatus;

    @NotNull(message = "Le type  de bon commande est obligatoire")
    private PurchaseOrderType purchaseOrderType;

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

    @NotNull(message = "La condition de paiement est obligatoire")
    @ValidEnum(enumClass = PaymentCondition.class, message = "Condition de paiement invalide")
    private String paymentCondition;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date exchangeRateReferenceDate;

    private Double appliedExchangeRate;

    @ValidEnum(enumClass = ExchangeRateSource.class, message = "Source du taux invalide")
    private String exchangeRateSource;

    @NotNull(message = "Le partenaire est obligatoire")
    @Setter
    private String partner;

    //@Valid
    //@NotEmpty(message = "Au moins une ligne de facture est obligatoire")
    @Setter
    private List<PurchaseOrderItemCreateDTO> purchaseOrderItems;

    // 👉 Document (like rne/patente)
    @NotNull(message = "Le document de bon de commande est obligatoire")
    private MultipartFile purchaseOrderDocument;
}
