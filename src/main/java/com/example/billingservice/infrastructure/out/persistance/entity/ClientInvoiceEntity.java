package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.InvoiceType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("SALE")
@Getter
@Setter
public class ClientInvoiceEntity extends InvoiceEntity{
    @Override
    public InvoiceType getInvoiceType() {
        return InvoiceType.SALE;
    }
}
