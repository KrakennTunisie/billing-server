package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.InvoiceType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("PURCHASE")
@Getter
@Setter
public class SupplierInvoiceEntity extends InvoiceEntity{
    @Override
    public InvoiceType getInvoiceType() {
        return InvoiceType.PURCHASE;
    }
}
