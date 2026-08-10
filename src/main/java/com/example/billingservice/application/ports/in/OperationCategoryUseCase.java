package com.example.billingservice.application.ports.in;

import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseItemOperationCategoryDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseItemOperationCategoryPageItem;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;



public interface OperationCategoryUseCase extends BaseSettingUseCase<
        BaseItemOperationCategoryPageItem,
        BaseItemOperationCategoryDTO,
        BaseSettingCreateDTO,
        BaseSettingUpdateDTO
        > {





}
