package com.example.billingservice.infrastructure.out.persistance.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("SUPPLIER")
@Getter
@Setter
public class SupplierEntity extends PartnerEntity {

}
