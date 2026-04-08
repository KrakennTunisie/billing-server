package com.example.billingservice.infrastructure.out.persistance.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class PurchaseOrderCreateDTO {
    private String reference;
    private Date orderDate;
    private Double totalAmountExclTax;
    private Double totalAmountInclTax;
}
