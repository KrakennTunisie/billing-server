package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.SettingType;
import com.example.billingservice.domain.model.BaseItemOperationCategory;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseItemOperationCategoryDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseItemOperationCategoryPageItem;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.BaseItemOperationCategoryEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OperationCategoryMapper {

    public BaseItemOperationCategoryEntity toEntity(BaseItemOperationCategory operationCategory){
        if(operationCategory==null){
            return null;
        }
        BaseItemOperationCategoryEntity operationCategoryEntity = new BaseItemOperationCategoryEntity();
        operationCategoryEntity.setIdOperationCategory(operationCategory.getIdOperationCategory());
        operationCategoryEntity.setCode(operationCategory.getCode());
        operationCategoryEntity.setLabel(operationCategory.getLabel());
        operationCategoryEntity.setDescription(operationCategory.getDescription());
        operationCategoryEntity.setActive(operationCategory.isActive());
        return operationCategoryEntity;
    }

    public BaseItemOperationCategory entityToModel(BaseItemOperationCategoryEntity operationCategoryEntity){
        if(operationCategoryEntity==null){
            return null;
        }
        return BaseItemOperationCategory.builder()
                .idOperationCategory(operationCategoryEntity.getIdOperationCategory())
                .code(operationCategoryEntity.getCode())
                .label(operationCategoryEntity.getLabel())
                .description(operationCategoryEntity.getDescription())
                .isActive(operationCategoryEntity.isActive())
                .createdAt(operationCategoryEntity.getCreatedAt())
                .updatedAt(operationCategoryEntity.getUpdatedAt())
                .build();
    }

    public BaseItemOperationCategory createDtoToModel(BaseSettingCreateDTO operationCategoryCreateDTO, String code){
        if(operationCategoryCreateDTO==null){
            return null;
        }
        return BaseItemOperationCategory.builder()
                .code(code)
                .label(operationCategoryCreateDTO.getLabel())
                .description(operationCategoryCreateDTO.getDescription())
                .isActive(true)
                .build();
    }

    public BaseItemOperationCategory updateDtoToModel(
            BaseSettingUpdateDTO operationCategoryUpdateDTO, UUID idOperationCategory
    ){

        if(operationCategoryUpdateDTO==null){
            return null;
        }
        return BaseItemOperationCategory.builder()
                .idOperationCategory(idOperationCategory)
                .code(operationCategoryUpdateDTO.getCode())
                .label(operationCategoryUpdateDTO.getLabel())
                .description(operationCategoryUpdateDTO.getDescription())
                .isActive(true)
                .build();
    }

    public BaseItemOperationCategoryDTO modelToDTO(BaseItemOperationCategory operationCategory){
        if(operationCategory==null){
            return null;
        }
        return BaseItemOperationCategoryDTO.builder()
                .idOperationCategory(operationCategory.getIdOperationCategory())
                .code(operationCategory.getCode())
                .label(operationCategory.getLabel())
                .description(operationCategory.getDescription())
                .isActive(operationCategory.isActive())
                .settingType(SettingType.OPERATION_CATEGORY)
                .createdAt(operationCategory.getCreatedAt())
                .updatedAt(operationCategory.getUpdatedAt())
                .build();
    }

    public BaseItemOperationCategoryPageItem modelToPageItem(BaseItemOperationCategory operationCategory){
        if(operationCategory==null){
            return null;
        }
        return BaseItemOperationCategoryPageItem.builder()
                .idOperationCategory(operationCategory.getIdOperationCategory())
                .code(operationCategory.getCode())
                .label(operationCategory.getLabel())
                .isActive(operationCategory.isActive())
                .settingType(SettingType.OPERATION_CATEGORY)
                .build();
    }


}
