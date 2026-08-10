package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.SettingType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class BasePaymentConditionDTO {
    private UUID idPaymentCondition;
    private String code;
    private String label;
    private String description;
    private boolean isActive;
    private SettingType settingType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
