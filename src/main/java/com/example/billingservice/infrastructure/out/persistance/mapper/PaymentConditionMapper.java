package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.SettingType;
import com.example.billingservice.domain.model.PaymentCondition;
import com.example.billingservice.infrastructure.out.persistance.dto.BasePaymentConditionDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BasePaymentConditionPageItem;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.PaymentConditionEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentConditionMapper {


    public PaymentConditionEntity toEntity(PaymentCondition paymentCondition){
        if(paymentCondition==null){
            return null;
        }
        PaymentConditionEntity paymentConditionEntity = new PaymentConditionEntity();
        paymentConditionEntity.setIdPaymentCondition(paymentCondition.getIdPaymentCondition());
        paymentConditionEntity.setCode(paymentCondition.getCode());
        paymentConditionEntity.setLabel(paymentCondition.getLabel());
        paymentConditionEntity.setDescription(paymentCondition.getDescription());
        paymentConditionEntity.setActive(paymentCondition.isActive());
        return paymentConditionEntity;
    }

    public PaymentCondition entityToModel(PaymentConditionEntity paymentConditionEntity){
        if(paymentConditionEntity==null){
            return null;
        }
        return PaymentCondition.builder()
                .idPaymentCondition(paymentConditionEntity.getIdPaymentCondition())
                .code(paymentConditionEntity.getCode())
                .label(paymentConditionEntity.getLabel())
                .description(paymentConditionEntity.getDescription())
                .isActive(paymentConditionEntity.isActive())
                .createdAt(paymentConditionEntity.getCreatedAt())
                .updatedAt(paymentConditionEntity.getUpdatedAt())
                .build();
    }

    public PaymentCondition createDtoToModel(BaseSettingCreateDTO paymentConditionCreateDTO, String code){
        if(paymentConditionCreateDTO==null){
            return null;
        }
        return PaymentCondition.builder()
                .code(code)
                .label(paymentConditionCreateDTO.getLabel())
                .description(paymentConditionCreateDTO.getDescription())
                .isActive(true)
                .build();
    }

    public PaymentCondition updateDtoToModel(
            BaseSettingUpdateDTO paymentConditionUpdateDTO, UUID idPaymentCondition
    ){

        if(paymentConditionUpdateDTO==null){
            return null;
        }
        return PaymentCondition.builder()
                .idPaymentCondition(idPaymentCondition)
                .code(paymentConditionUpdateDTO.getCode())
                .label(paymentConditionUpdateDTO.getLabel())
                .description(paymentConditionUpdateDTO.getDescription())
                .isActive(true)
                .build();
    }

    public BasePaymentConditionDTO modelToDTO(PaymentCondition paymentCondition){
        if(paymentCondition==null){
            return null;
        }
        return BasePaymentConditionDTO.builder()
                .idPaymentCondition(paymentCondition.getIdPaymentCondition())
                .code(paymentCondition.getCode())
                .label(paymentCondition.getLabel())
                .description(paymentCondition.getDescription())
                .isActive(paymentCondition.isActive())
                .settingType(SettingType.PAYMENT_CONDITION)

                .createdAt(paymentCondition.getCreatedAt())
                .updatedAt(paymentCondition.getUpdatedAt())
                .build();
    }

    public BasePaymentConditionPageItem modelToPageItem(PaymentCondition paymentCondition){
        if(paymentCondition==null){
            return null;
        }
        return BasePaymentConditionPageItem.builder()
                .idPaymentCondition(paymentCondition.getIdPaymentCondition())
                .code(paymentCondition.getCode())
                .label(paymentCondition.getLabel())
                .isActive(paymentCondition.isActive())
                .settingType(SettingType.PAYMENT_CONDITION)
                .build();
    }

}
