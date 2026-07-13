package com.example.billingservice.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
public class BaseItemOperationCategory  extends BaseSetting{
    private UUID idOperationCategory;

}
