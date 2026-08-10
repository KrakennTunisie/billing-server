package com.example.billingservice.application.ports.in;

import java.util.List;
import java.util.UUID;

public interface BaseSettingUseCase<
        PAGE_ITEM,
        DTO,
        CREATE_DTO,
        UPDATE_DTO> {

    List<PAGE_ITEM> getAll();

    List<PAGE_ITEM> getAllActive();

    DTO getById(UUID id);

    DTO getByCode(String code);

    DTO getByLabel(String label);

    DTO create(CREATE_DTO dto);

    DTO update(UPDATE_DTO dto, String id);

    boolean existsById(UUID id);

    boolean existsByCode(String code);

    boolean existsByLabel(String label);

    void activate(UUID id);

    void deactivate(UUID id);
}
