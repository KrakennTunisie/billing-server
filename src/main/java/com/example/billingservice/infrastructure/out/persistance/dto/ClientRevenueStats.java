package com.example.billingservice.infrastructure.out.persistance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientRevenueStats {
    private String period;
    private String monthLabel;
    private Double revenueHT;
    private Double revenueTVA;
    private Double revenueTTC;
    private long nombreFactures;
}
