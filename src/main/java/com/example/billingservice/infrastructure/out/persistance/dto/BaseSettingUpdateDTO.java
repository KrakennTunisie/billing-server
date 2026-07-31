package com.example.billingservice.infrastructure.out.persistance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseSettingUpdateDTO {

    @NotBlank(message = "le code est obligatoire")
    private String code;

    @NotBlank(message = "label est obligatoire")
    private String label;

    private String description;
}
