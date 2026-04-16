package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.OperationCategory;
import com.example.billingservice.infrastructure.out.persistance.validators.ValidEnum;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class PurchaseOrderItemCreateDTO {

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantity;

    @NotNull(message = "Le prix unitaire HT est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix unitaire HT doit être positif")
    private Double unityPriceEXclTax;

    @NotNull(message = "Le taux de TVA est obligatoire")
    @DecimalMin(value = "0.0", message = "Le taux de TVA doit être positif ou nul")
    @DecimalMax(value = "100.0", message = "Le taux de TVA ne peut pas dépasser 100")
    private Double vatRate;

    @NotNull(message = "Le montant de TVA de l'article est obligatoire")
    @DecimalMin(value = "0.0", message = "Le montant de TVA doit être positif ou nul")
    private Double itemTaxAmount;

    @NotNull(message = "La catégorie d'opération est obligatoire")
    @ValidEnum(enumClass = OperationCategory.class, message = "Catégorie d'opération invalide")
    private String operationCategory;
}
