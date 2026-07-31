package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.SettingType;
import com.example.billingservice.domain.model.TVARate;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseTVARateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseTVARatePageItem;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.TvaRateEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TvaRateMapper {


    public TvaRateEntity toEntity(TVARate TvaRate){
        if(TvaRate==null){
            return null;
        }
        TvaRateEntity TvaRateEntity = new TvaRateEntity();
        TvaRateEntity.setIdTvaRate(TvaRate.getIdTvaRate());
        TvaRateEntity.setCode(TvaRate.getCode());
        TvaRateEntity.setLabel(TvaRate.getLabel().strip());
        TvaRateEntity.setDescription(TvaRate.getDescription());
        TvaRateEntity.setActive(TvaRate.isActive());
        return TvaRateEntity;
    }

    public TVARate entityToModel(TvaRateEntity TvaRateEntity){
        if(TvaRateEntity==null){
            return null;
        }
        return TVARate.builder()
                .idTvaRate(TvaRateEntity.getIdTvaRate())
                .code(TvaRateEntity.getCode())
                .label(TvaRateEntity.getLabel())
                .description(TvaRateEntity.getDescription())
                .isActive(TvaRateEntity.isActive())
                .createdAt(TvaRateEntity.getCreatedAt())
                .updatedAt(TvaRateEntity.getUpdatedAt())
                .build();
    }

    public TVARate createDtoToModel(BaseSettingCreateDTO tvaRateCreateDTO, String code){
        if(tvaRateCreateDTO==null){
            return null;
        }
        return TVARate.builder()
                .code(code)
                .label(tvaRateCreateDTO.getLabel())
                .description(tvaRateCreateDTO.getDescription())
                .isActive(true)
                .build();
    }

    public TVARate updateDtoToModel(
            BaseSettingUpdateDTO TvaRateUpdateDTO, UUID idTvaRate
    ){

        if(TvaRateUpdateDTO==null){
            return null;
        }
        return TVARate.builder()
                .idTvaRate(idTvaRate)
                .code(TvaRateUpdateDTO.getCode())
                .label(TvaRateUpdateDTO.getLabel())
                .description(TvaRateUpdateDTO.getDescription())
                .isActive(true)
                .build();
    }

    public BaseTVARateDTO modelToDTO(TVARate tvaRate){
        if(tvaRate==null){
            return null;
        }
        return BaseTVARateDTO.builder()
                .idTVARate(tvaRate.getIdTvaRate())
                .code(tvaRate.getCode())
                .label(tvaRate.getLabel())
                .description(tvaRate.getDescription())
                .isActive(tvaRate.isActive())
                .settingType(SettingType.TVA_RATE)
                .createdAt(tvaRate.getCreatedAt())
                .updatedAt(tvaRate.getUpdatedAt())
                .build();
    }

    public BaseTVARatePageItem modelToPageItem(TVARate tvaRate){
        if(tvaRate==null){
            return null;
        }
        return BaseTVARatePageItem.builder()
                .idTVARate(tvaRate.getIdTvaRate())
                .code(tvaRate.getCode())
                .label(tvaRate.getLabel())
                .isActive(tvaRate.isActive())
                .settingType(SettingType.TVA_RATE)
                .build();
    }

}
