package com.example.billingservice.application.ports.in;

import com.example.billingservice.infrastructure.out.persistance.dto.BasePaymentConditionDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BasePaymentConditionPageItem;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;

public interface PaymentConditionUseCase extends BaseSettingUseCase<
        BasePaymentConditionPageItem,
        BasePaymentConditionDTO,
        BaseSettingCreateDTO,
        BaseSettingUpdateDTO
        > {
}
