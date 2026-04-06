package com.example.billingservice.infrastructure.out.persistance.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("CLIENT")
@Getter
@Setter
public class CustomerEntity extends PartnerEntity{
}
