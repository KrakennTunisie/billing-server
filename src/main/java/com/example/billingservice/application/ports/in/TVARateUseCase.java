package com.example.billingservice.application.ports.in;

import com.example.billingservice.infrastructure.out.persistance.dto.*;

public interface TVARateUseCase  extends BaseSettingUseCase<
        BaseTVARatePageItem,
        BaseTVARateDTO,
        BaseSettingCreateDTO,
        BaseSettingUpdateDTO
        >{
}
