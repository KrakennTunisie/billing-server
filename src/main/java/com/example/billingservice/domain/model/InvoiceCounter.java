package com.example.billingservice.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceCounter {
    private Integer year;
    private Long lastSequence;
}
