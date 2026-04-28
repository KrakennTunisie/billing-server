package com.example.billingservice.infrastructure.out.persistance.entity;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("PURCHASE")
@Getter
@Setter
public class SupplierPurchaseOrderEntity extends PurchaseOrderEntity{
}
