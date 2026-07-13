package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.TvaRateRepositoryPort;
import com.example.billingservice.domain.model.TVARate;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseTVARateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseTVARatePageItem;
import com.example.billingservice.infrastructure.out.persistance.entity.TvaRateEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.TvaRateMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.TvaRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class TvaRateRepositoryAdapter implements TvaRateRepositoryPort {

    private final TvaRateRepository tvaRateRepository;
    private final TvaRateMapper tvaRateMapper;

    @Override
    public List<BaseTVARatePageItem> getAll() {
        List<TvaRateEntity> entities = tvaRateRepository.findAll();

        return entities.stream().map(
                tvaRateMapper::entityToModel
        ).map(
                tvaRateMapper::modelToPageItem
        ).toList();
    }

    @Override
    public List<BaseTVARatePageItem> getAllActive() {
        List<TvaRateEntity> entities = tvaRateRepository.findAllByIsActive(true);

        return entities.stream().map(
                tvaRateMapper::entityToModel
        ).map(
                tvaRateMapper::modelToPageItem
        ).toList();
    }

    @Override
    public BaseTVARateDTO create(BaseSettingCreateDTO baseSettingCreateDTO, String code) {
        TVARate tvaRate = tvaRateMapper.createDtoToModel(baseSettingCreateDTO, code);
        TvaRateEntity tvaRateEntity = tvaRateMapper.toEntity(tvaRate);
        return tvaRateMapper
                .modelToDTO(
                        tvaRateMapper.entityToModel(
                                tvaRateRepository.save(tvaRateEntity))
                );
    }

    @Override
    public BaseTVARateDTO update(BaseSettingUpdateDTO baseSettingUpdateDTO, UUID id) {
        TVARate tvaRate = tvaRateMapper.updateDtoToModel(baseSettingUpdateDTO, id);
        TvaRateEntity tvaRateEntity = tvaRateMapper.toEntity(tvaRate);
        return tvaRateMapper
                .modelToDTO(
                        tvaRateMapper.entityToModel(
                                tvaRateRepository.save(tvaRateEntity))
                );
    }

    @Override
    public BaseTVARateDTO getById(UUID id) {
        TvaRateEntity tvaRateEntity =
                tvaRateRepository.getReferenceById(id);

        return tvaRateMapper.modelToDTO(tvaRateMapper.entityToModel(tvaRateEntity));
    }

    @Override
    public BaseTVARateDTO getByCode(String code) {
        TvaRateEntity tvaRateEntity =
                tvaRateRepository.getTvaRateEntityByCode(code);

        return tvaRateMapper.modelToDTO(tvaRateMapper.entityToModel(tvaRateEntity));
    }

    @Override
    public BaseTVARateDTO getByLabel(String label) {
        TvaRateEntity tvaRateEntity =
                tvaRateRepository.getTvaRateEntityByLabel(label);

        return tvaRateMapper.modelToDTO(tvaRateMapper.entityToModel(tvaRateEntity));
    }

    @Override
    public boolean existsById(UUID id) {
        return tvaRateRepository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return tvaRateRepository.existsByCode(code);
    }

    @Override
    public boolean existsByLabel(String label) {
        return tvaRateRepository.existsByLabel(label);
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, UUID id) {
        return tvaRateRepository.existsByCodeAndIdTvaRateNot(code, id);
    }

    @Override
    public boolean existsByLabelAndIdNot(String label, UUID id) {
        return tvaRateRepository.existsByLabelAndIdTvaRateNot(label, id);
    }

    @Override
    public void activate(UUID id) {
        TvaRateEntity tvaRateEntity = tvaRateRepository.getReferenceById(id);
        tvaRateEntity.setActive(true);
        tvaRateRepository.save(tvaRateEntity);

    }

    @Override
    public void deactivate(UUID id) {
        TvaRateEntity tvaRateEntity = tvaRateRepository.getReferenceById(id);
        tvaRateEntity.setActive(false);
        tvaRateRepository.save(tvaRateEntity);
    }
}
