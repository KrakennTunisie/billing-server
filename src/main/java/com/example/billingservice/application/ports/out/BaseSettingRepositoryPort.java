package com.example.billingservice.application.ports.out;

import java.util.List;
import java.util.UUID;

public interface BaseSettingRepositoryPort<
        PAGE_ITEM,
        DTO,
        CREATE_DTO,
        UPDATE_DTO> {

    List<PAGE_ITEM> getAll();

    List<PAGE_ITEM> getAllActive();

    DTO create(CREATE_DTO dto, String code);

    DTO update(UPDATE_DTO dto, UUID id);

    DTO getById(UUID id);

    DTO getByCode(String code);

    DTO getByLabel(String label);

    boolean existsById(UUID id);

    boolean existsByCode(String code);

    boolean existsByLabel(String label);

    boolean existsByCodeAndIdNot(String code, UUID id);

    boolean existsByLabelAndIdNot(String label, UUID id);

    void activate(UUID id);

    void deactivate(UUID id);
}