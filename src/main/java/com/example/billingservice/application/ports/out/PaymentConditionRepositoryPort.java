package com.example.billingservice.application.ports.out;

import com.example.billingservice.infrastructure.out.persistance.dto.BasePaymentConditionDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BasePaymentConditionPageItem;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;

public interface PaymentConditionRepositoryPort
        extends BaseSettingRepositoryPort<BasePaymentConditionPageItem,
        BasePaymentConditionDTO,
        BaseSettingCreateDTO,
        BaseSettingUpdateDTO>
{
}
