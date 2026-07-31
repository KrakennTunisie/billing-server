package com.example.billingservice.infrastructure.out.persistance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseSettingCreateDTO {
    // auto generated
   // private String code;
    @NotBlank(message = "Label est obligatoire")
    private String label;

    private String description;
}
