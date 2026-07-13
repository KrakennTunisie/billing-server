package com.example.billingservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseSetting {

    private String code;
    private String label;
    private String description;

    private boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
