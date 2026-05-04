package com.example.billingservice.infrastructure.out.persistance.dto;

import java.math.BigDecimal;

public record StatAmount(BigDecimal eur,
                         BigDecimal tnd,
                         Long count) {}
