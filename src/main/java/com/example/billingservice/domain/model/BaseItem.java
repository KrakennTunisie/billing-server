package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.OperationCategory;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseItem {
    private String description;
    private Integer quantity;
    private Double unityPriceEXclTax;
    private Double vatRate;
    private Double itemTotalExclTax;
    private Double itemTaxAmount;
    private Double itemTotalInclTax;
    private OperationCategory operationCategory;
}
