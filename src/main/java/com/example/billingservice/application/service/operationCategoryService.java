package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.OperationCategoryUseCase;
import com.example.billingservice.application.ports.out.OperationCategoryRepositoryPort;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseItemOperationCategoryDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseItemOperationCategoryPageItem;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class operationCategoryService implements OperationCategoryUseCase {

    private final OperationCategoryRepositoryPort operationCategoryRepositoryPort;

    @Override
    public List<BaseItemOperationCategoryPageItem> getAll() {
        return operationCategoryRepositoryPort.getAll();
    }

    @Override
    public List<BaseItemOperationCategoryPageItem> getAllActive() {
        return operationCategoryRepositoryPort.getAllActive();
    }

    @Override
    public BaseItemOperationCategoryDTO getById(UUID id) {
        if(!operationCategoryRepositoryPort.existsById(id)){
            throw BillingException.notFound("Catégorie service", String.valueOf(id));
        }
        return operationCategoryRepositoryPort.getById(id);
    }

    @Override
    public BaseItemOperationCategoryDTO getByCode(String code) {
        if(!operationCategoryRepositoryPort.existsByCode(code)){
            throw BillingException.notFound("Catégorie service", code);
        }
        return operationCategoryRepositoryPort.getByCode(code);    }

    @Override
    public BaseItemOperationCategoryDTO getByLabel(String label) {
        if(!operationCategoryRepositoryPort.existsByLabel(label)){
            throw BillingException.notFound("Catégorie service", label);
        }
        return operationCategoryRepositoryPort.getByLabel(label);    }

    @Override
    public BaseItemOperationCategoryDTO create(BaseSettingCreateDTO baseSettingCreateDTO) {
        if(operationCategoryRepositoryPort.existsByLabel(baseSettingCreateDTO.getLabel())){
            throw BillingException
                    .alreadyExists("Catégorie service", "label", baseSettingCreateDTO.getLabel());
        }
        String newCode = "OP"+Math.random();

        return operationCategoryRepositoryPort.create(baseSettingCreateDTO,newCode);
    }

    @Override
    public BaseItemOperationCategoryDTO update(BaseSettingUpdateDTO operationCategoryUpdateDTO, String id) {
        if(!operationCategoryRepositoryPort.existsById(UUID.fromString(id))){
            throw BillingException.alreadyExists("Catégorie service", "id", id);
        }
        if(operationCategoryRepositoryPort.existsByCodeAndIdNot(
                operationCategoryUpdateDTO.getCode(), UUID.fromString(id)))
        {
            throw BillingException.alreadyExists("Catégorie service", "code", operationCategoryUpdateDTO.getCode());
        }

        if(operationCategoryRepositoryPort.existsByLabelAndIdNot(
                operationCategoryUpdateDTO.getLabel(), UUID.fromString(id)))
        {
            throw BillingException.alreadyExists("Catégorie service", "label", operationCategoryUpdateDTO.getLabel());
        }

        return operationCategoryRepositoryPort.update(operationCategoryUpdateDTO, UUID.fromString(id));
    }

    @Override
    public boolean existsById(UUID idOperationCategory) {
        return operationCategoryRepositoryPort.existsById(idOperationCategory);
    }

    @Override
    public boolean existsByCode(String code) {
        return operationCategoryRepositoryPort.existsByCode(code);
    }

    @Override
    public boolean existsByLabel(String label) {
        return operationCategoryRepositoryPort.existsByLabel(label);
    }

    @Override
    public void activate(UUID id) {
        operationCategoryRepositoryPort.activate(id);
    }

    @Override
    public void deactivate(UUID id) {
        operationCategoryRepositoryPort.deactivate(id);
    }


}
