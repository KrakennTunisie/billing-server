package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.OperationCategory;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseItemEntity {

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    // Quantity can be decimal (ex: 1.5 kg, 2.25 hours)
    private Integer quantity;

    private Double unityPriceEXclTax;

    private Double vatRate;

    @Enumerated(EnumType.STRING)
    private OperationCategory operationCategory;
}
