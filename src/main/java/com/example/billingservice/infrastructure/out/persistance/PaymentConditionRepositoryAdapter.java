package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.PaymentConditionRepositoryPort;
import com.example.billingservice.domain.model.PaymentCondition;
import com.example.billingservice.infrastructure.out.persistance.dto.BasePaymentConditionDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BasePaymentConditionPageItem;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.PaymentConditionEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.PaymentConditionMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.PaymentConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class PaymentConditionRepositoryAdapter implements PaymentConditionRepositoryPort {

    private final PaymentConditionRepository paymentConditionRepository;
    private final PaymentConditionMapper paymentConditionMapper;

    @Override
    public List<BasePaymentConditionPageItem> getAll() {
        List<PaymentConditionEntity> entities = paymentConditionRepository.findAll();

        return entities.stream().map(
                paymentConditionMapper::entityToModel
        ).map(
                paymentConditionMapper::modelToPageItem
        ).toList();
    }

    @Override
    public List<BasePaymentConditionPageItem> getAllActive() {
        List<PaymentConditionEntity> entities = paymentConditionRepository.findAllByIsActive(true);

        return entities.stream().map(
                paymentConditionMapper::entityToModel
        ).map(
                paymentConditionMapper::modelToPageItem
        ).toList();
    }

    @Override
    public BasePaymentConditionDTO create(BaseSettingCreateDTO baseSettingCreateDTO, String code) {
        PaymentCondition paymentCondition = paymentConditionMapper.createDtoToModel(baseSettingCreateDTO, code);
        PaymentConditionEntity paymentConditionEntity = paymentConditionMapper.toEntity(paymentCondition);
        return paymentConditionMapper
                .modelToDTO(
                        paymentConditionMapper.entityToModel(
                                paymentConditionRepository.save(paymentConditionEntity))
                );
    }

    @Override
    public BasePaymentConditionDTO update(BaseSettingUpdateDTO baseSettingUpdateDTO, UUID id) {
        PaymentCondition paymentCondition = paymentConditionMapper.updateDtoToModel(baseSettingUpdateDTO, id);
        PaymentConditionEntity paymentConditionEntity = paymentConditionMapper.toEntity(paymentCondition);
        return paymentConditionMapper
                .modelToDTO(
                        paymentConditionMapper.entityToModel(
                                paymentConditionRepository.save(paymentConditionEntity))
                );
    }

    @Override
    public BasePaymentConditionDTO getById(UUID id) {
        PaymentConditionEntity paymentConditionEntity =
                paymentConditionRepository.getReferenceById(id);

        return paymentConditionMapper.modelToDTO(paymentConditionMapper.entityToModel(paymentConditionEntity));
    }

    @Override
    public BasePaymentConditionDTO getByCode(String code) {
        PaymentConditionEntity paymentConditionEntity =
                paymentConditionRepository.getPaymentConditionEntityByCode(code);

        return paymentConditionMapper.modelToDTO(paymentConditionMapper.entityToModel(paymentConditionEntity));
    }

    @Override
    public BasePaymentConditionDTO getByLabel(String label) {
        PaymentConditionEntity paymentConditionEntity =
                paymentConditionRepository.getPaymentConditionEntityByLabel(label);

        return paymentConditionMapper.modelToDTO(paymentConditionMapper.entityToModel(paymentConditionEntity));
    }

    @Override
    public boolean existsById(UUID id) {
        return paymentConditionRepository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return paymentConditionRepository.existsByCode(code);
    }

    @Override
    public boolean existsByLabel(String label) {
        return paymentConditionRepository.existsByLabel(label);
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, UUID id) {
        return paymentConditionRepository.existsByCodeAndIdPaymentConditionNot(code, id);
    }

    @Override
    public boolean existsByLabelAndIdNot(String label, UUID id) {
        return paymentConditionRepository.existsByLabelAndIdPaymentConditionNot(label, id);
    }

    @Override
    public void activate(UUID id) {
        PaymentConditionEntity paymentConditionEntity = paymentConditionRepository.getReferenceById(id);
        paymentConditionEntity.setActive(true);
        paymentConditionRepository.save(paymentConditionEntity);
    }

    @Override
    public void deactivate(UUID id) {
        PaymentConditionEntity paymentConditionEntity = paymentConditionRepository.getReferenceById(id);
        paymentConditionEntity.setActive(false);
        paymentConditionRepository.save(paymentConditionEntity);
    }
}
