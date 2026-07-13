package com.example.billingservice.application.ports.out;

import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseTVARateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseTVARatePageItem;

public interface TvaRateRepositoryPort
        extends BaseSettingRepositoryPort<
        BaseTVARatePageItem,
        BaseTVARateDTO,
        BaseSettingCreateDTO,
        BaseSettingUpdateDTO>
{
}
