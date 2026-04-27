package com.example.billingservice.infrastructure.out.persistance.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("SALE")
@Getter
@Setter
public class ClientPurchaseOrderEntity extends PurchaseOrderEntity {
}
