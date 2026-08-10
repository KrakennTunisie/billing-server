package com.example.billingservice.application.ports.out;

import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseItemOperationCategoryDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseItemOperationCategoryPageItem;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;

public interface OperationCategoryRepositoryPort extends BaseSettingRepositoryPort<
        BaseItemOperationCategoryPageItem,
        BaseItemOperationCategoryDTO,
        BaseSettingCreateDTO,
        BaseSettingUpdateDTO

        >  {

}
