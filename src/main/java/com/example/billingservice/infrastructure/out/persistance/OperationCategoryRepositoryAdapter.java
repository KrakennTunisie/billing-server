package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.OperationCategoryRepositoryPort;
import com.example.billingservice.domain.model.BaseItemOperationCategory;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseItemOperationCategoryDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseItemOperationCategoryPageItem;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.BaseItemOperationCategoryEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.OperationCategoryMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.OperationCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class OperationCategoryRepositoryAdapter implements OperationCategoryRepositoryPort {

    private final OperationCategoryRepository operationCategoryRepository;
    private final OperationCategoryMapper operationCategoryMapper;

    @Override
    public List<BaseItemOperationCategoryPageItem> getAll() {
        List<BaseItemOperationCategoryEntity> entities = operationCategoryRepository.findAll();

        return entities.stream().map(
                operationCategoryMapper::entityToModel
        ).map(
                operationCategoryMapper::modelToPageItem
        ).toList();
    }

    @Override
    public List<BaseItemOperationCategoryPageItem> getAllActive() {
        List<BaseItemOperationCategoryEntity> entities = operationCategoryRepository.findAllByIsActive(true);

        return entities.stream().map(
                operationCategoryMapper::entityToModel
        ).map(
                operationCategoryMapper::modelToPageItem
        ).toList();
    }

    @Override
    public BaseItemOperationCategoryDTO create(BaseSettingCreateDTO baseSettingCreateDTO, String code) {
        BaseItemOperationCategory operationCategory = operationCategoryMapper.createDtoToModel(baseSettingCreateDTO, code);
        BaseItemOperationCategoryEntity operationCategoryEntity = operationCategoryMapper.toEntity(operationCategory);
        return operationCategoryMapper
                .modelToDTO(
                        operationCategoryMapper.entityToModel(
                                operationCategoryRepository.save(operationCategoryEntity))
                );
    }

    @Override
    public BaseItemOperationCategoryDTO update(BaseSettingUpdateDTO baseSettingUpdateDTO, UUID id) {
        BaseItemOperationCategory operationCategory = operationCategoryMapper.updateDtoToModel(baseSettingUpdateDTO, id);
        BaseItemOperationCategoryEntity operationCategoryEntity = operationCategoryMapper.toEntity(operationCategory);
        return operationCategoryMapper
                .modelToDTO(
                        operationCategoryMapper.entityToModel(
                                operationCategoryRepository.save(operationCategoryEntity))
                );    }

    @Override
    public BaseItemOperationCategoryDTO getById(UUID id) {
        BaseItemOperationCategoryEntity operationCategoryEntity =
                operationCategoryRepository.getReferenceById(id);

        return operationCategoryMapper.modelToDTO(operationCategoryMapper.entityToModel(operationCategoryEntity));
    }

    @Override
    public BaseItemOperationCategoryDTO getByCode(String code) {

        BaseItemOperationCategoryEntity operationCategoryEntity =
                operationCategoryRepository.getBaseItemOperationCategoryEntityByCode(code);

        return operationCategoryMapper.modelToDTO(operationCategoryMapper.entityToModel(operationCategoryEntity));    }

    @Override
    public BaseItemOperationCategoryDTO getByLabel(String label) {
        BaseItemOperationCategoryEntity operationCategoryEntity =
                operationCategoryRepository.getBaseItemOperationCategoryEntityByLabel(label);

        return operationCategoryMapper.modelToDTO(operationCategoryMapper.entityToModel(operationCategoryEntity));
    }

    @Override
    public boolean existsById(UUID idOperationCategory) {
        return operationCategoryRepository.existsById(idOperationCategory);
    }

    @Override
    public boolean existsByCode(String code) {
        return operationCategoryRepository.existsByCode(code);
    }

    @Override
    public boolean existsByLabel(String label) {
        return operationCategoryRepository.existsByLabel(label);
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, UUID id) {
        return operationCategoryRepository.existsByCodeAndIdOperationCategoryNot(code, id);
    }

    @Override
    public boolean existsByLabelAndIdNot(String label, UUID id) {
        return operationCategoryRepository.existsByLabelAndIdOperationCategoryNot(label, id);
    }

    @Override
    public void activate(UUID id) {
        BaseItemOperationCategoryEntity operationCategoryEntity =
                operationCategoryRepository.getReferenceById(id);
        operationCategoryEntity.setActive(true);

        operationCategoryRepository.save(operationCategoryEntity);
    }

    @Override
    public void deactivate(UUID id) {
        BaseItemOperationCategoryEntity operationCategoryEntity = operationCategoryRepository.getReferenceById(id);
        operationCategoryEntity.setActive(false);
        operationCategoryRepository.save(operationCategoryEntity);

    }


}
