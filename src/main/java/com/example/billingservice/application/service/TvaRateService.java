package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.TVARateUseCase;
import com.example.billingservice.application.ports.out.TvaRateRepositoryPort;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseTVARateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseTVARatePageItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class TvaRateService implements TVARateUseCase {

    private final TvaRateRepositoryPort tvaRateRepositoryPort;
    private static final Pattern TVA_LABEL_PATTERN = Pattern.compile("^\\d{1,3}%$");
    @Override
    public List<BaseTVARatePageItem> getAll() {
        return tvaRateRepositoryPort.getAll();
    }

    @Override
    public List<BaseTVARatePageItem> getAllActive() {
        return tvaRateRepositoryPort.getAllActive();
    }

    @Override
    public BaseTVARateDTO getById(UUID id) {
        if(!tvaRateRepositoryPort.existsById(id)){
            throw BillingException.notFound("Taux TVA", String.valueOf(id));
        }
        return tvaRateRepositoryPort.getById(id);
    }

    @Override
    public BaseTVARateDTO getByCode(String code) {
        if(!tvaRateRepositoryPort.existsByCode(code)){
            throw BillingException.notFound("Taux TVA", code);
        }
        return tvaRateRepositoryPort.getByCode(code);
    }

    @Override
    public BaseTVARateDTO getByLabel(String label) {
        if(!tvaRateRepositoryPort.existsByCode(label)){
            throw BillingException.notFound("Taux TVA", label);
        }
        return tvaRateRepositoryPort.getByLabel(label);
    }

    @Override
    public BaseTVARateDTO create(BaseSettingCreateDTO baseSettingCreateDTO) {

        validateLabel(baseSettingCreateDTO.getLabel());

        if(tvaRateRepositoryPort.existsByLabel(baseSettingCreateDTO.getLabel())){
            throw BillingException
                    .alreadyExists("Taux TVA", "label", baseSettingCreateDTO.getLabel());
        }
        String newCode = "TVA-"+Math.random();

        return tvaRateRepositoryPort.create(baseSettingCreateDTO,newCode);
    }

    @Override
    public BaseTVARateDTO update(BaseSettingUpdateDTO baseSettingUpdateDTO, String id) {

        validateLabel(baseSettingUpdateDTO.getLabel());

        if(!tvaRateRepositoryPort.existsById(UUID.fromString(id))){
            throw BillingException.alreadyExists("TVA", "id", id);
        }
        if(tvaRateRepositoryPort.existsByCodeAndIdNot(baseSettingUpdateDTO.getCode(), UUID.fromString(id)))
        {
            throw BillingException.alreadyExists("TVA", "code", baseSettingUpdateDTO.getCode());
        }

        if(tvaRateRepositoryPort.existsByLabelAndIdNot(
                baseSettingUpdateDTO.getLabel(), UUID.fromString(id)))
        {
            throw BillingException.alreadyExists("TVA", "label", baseSettingUpdateDTO.getLabel());
        }

        return tvaRateRepositoryPort.update(baseSettingUpdateDTO, UUID.fromString(id));
    }

    @Override
    public boolean existsById(UUID id) {
        return tvaRateRepositoryPort.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return tvaRateRepositoryPort.existsByCode(code);
    }

    @Override
    public boolean existsByLabel(String label) {
        return tvaRateRepositoryPort.existsByLabel(label);
    }

    @Override
    public void activate(UUID id) {
        if(!tvaRateRepositoryPort.existsById(id)){
            throw BillingException.notFound("Taux TVA", String.valueOf(id));
        }
        tvaRateRepositoryPort.activate(id);
    }

    @Override
    public void deactivate(UUID id) {
        if(!tvaRateRepositoryPort.existsById(id)){
            throw BillingException.notFound("Taux TVA", String.valueOf(id));
        }
        tvaRateRepositoryPort.deactivate(id);
    }

    private void validateLabel(String label) {
        if (!TVA_LABEL_PATTERN.matcher(label).matches()) {
            throw BillingException.badRequest(
                    "Le libellé doit être au format '20%' (1 à 3 chiffres suivis de %)."
            );
        }
    }
}
