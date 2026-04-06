package com.example.billingservice.infrastructure.out.persistance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvoiceCounterDTO {

    private Integer year;

    private Long lastSequence;
}
