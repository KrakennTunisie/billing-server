package com.example.billingservice.infrastructure.out.persistance.dto;


import com.example.billingservice.domain.enums.SettingType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class BaseItemOperationCategoryPageItem {

    private UUID idOperationCategory;
    private String code;
    private String label;
    private boolean isActive;
    private SettingType settingType;
}
