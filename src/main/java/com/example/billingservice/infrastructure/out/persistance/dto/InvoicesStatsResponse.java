package com.example.billingservice.infrastructure.out.persistance.dto;

public record InvoicesStatsResponse(StatAmount totalYear,
                                    StatAmount toCollectYear,
                                    StatAmount currentMonth) {
}
