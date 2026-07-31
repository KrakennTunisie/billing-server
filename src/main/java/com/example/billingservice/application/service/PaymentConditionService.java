package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.PaymentConditionUseCase;
import com.example.billingservice.application.ports.out.PaymentConditionRepositoryPort;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.infrastructure.out.persistance.dto.BasePaymentConditionDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BasePaymentConditionPageItem;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentConditionService implements PaymentConditionUseCase {

    private final PaymentConditionRepositoryPort paymentConditionRepositoryPort;

    @Override
    public List<BasePaymentConditionPageItem> getAll() {
        return paymentConditionRepositoryPort.getAll();
    }

    @Override
    public List<BasePaymentConditionPageItem> getAllActive() {
        return paymentConditionRepositoryPort.getAllActive();
    }

    @Override
    public BasePaymentConditionDTO getById(UUID id) {
        if(!paymentConditionRepositoryPort.existsById(id)){
            throw BillingException.notFound("Condition de Paiement", String.valueOf(id));
        }
        return paymentConditionRepositoryPort.getById(id);
    }

    @Override
    public BasePaymentConditionDTO getByCode(String code) {
        if(!paymentConditionRepositoryPort.existsByCode(code)){
            throw BillingException.notFound("Condition de Paiement", code);
        }
        return paymentConditionRepositoryPort.getByCode(code);
    }

    @Override
    public BasePaymentConditionDTO getByLabel(String label) {
        if(!paymentConditionRepositoryPort.existsByLabel(label)){
            throw BillingException.notFound("Condition de Paiement", label);
        }
        return paymentConditionRepositoryPort.getByLabel(label);
    }

    @Override
    public BasePaymentConditionDTO create(BaseSettingCreateDTO baseSettingCreateDTO) {
        if(paymentConditionRepositoryPort.existsByLabel(baseSettingCreateDTO.getLabel())){
            throw BillingException
                    .alreadyExists("Condition de paiement", "label", baseSettingCreateDTO.getLabel());
        }
        String newCode = "CP-"+Math.random();

        return paymentConditionRepositoryPort.create(baseSettingCreateDTO,newCode);
    }

    @Override
    public BasePaymentConditionDTO update(BaseSettingUpdateDTO baseSettingUpdateDTO, String id) {
        if(!paymentConditionRepositoryPort.existsById(UUID.fromString(id))){
            throw BillingException.alreadyExists("Condition de paiement", "id", id);
        }
        if(paymentConditionRepositoryPort.existsByCodeAndIdNot(
                baseSettingUpdateDTO.getCode(), UUID.fromString(id)))
        {
            throw BillingException.alreadyExists("Condition de paiement", "code", baseSettingUpdateDTO.getCode());
        }

        if(paymentConditionRepositoryPort.existsByLabelAndIdNot(
                baseSettingUpdateDTO.getLabel(), UUID.fromString(id)))
        {
            throw BillingException.alreadyExists("Condition de paiement", "label", baseSettingUpdateDTO.getLabel());
        }

        return paymentConditionRepositoryPort.update(baseSettingUpdateDTO, UUID.fromString(id));
    }

    @Override
    public boolean existsById(UUID id) {
        return paymentConditionRepositoryPort.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return paymentConditionRepositoryPort.existsByCode(code);
    }

    @Override
    public boolean existsByLabel(String label) {
        return paymentConditionRepositoryPort.existsByLabel(label);
    }

    @Override
    public void activate(UUID id) {
        if(!paymentConditionRepositoryPort.existsById(id)){
            throw BillingException.notFound("Condition de paiement", String.valueOf(id));
        }
        paymentConditionRepositoryPort.activate(id);
    }

    @Override
    public void deactivate(UUID id) {
        if(!paymentConditionRepositoryPort.existsById(id)){
            throw BillingException.notFound("Condition de paiement", String.valueOf(id));
        }
        paymentConditionRepositoryPort.deactivate(id);
    }
}
