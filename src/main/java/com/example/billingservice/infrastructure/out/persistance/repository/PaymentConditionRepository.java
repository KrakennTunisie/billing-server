package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.entity.PaymentConditionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentConditionRepository extends JpaRepository<PaymentConditionEntity, UUID> {

    List<PaymentConditionEntity> findAllByIsActive(boolean isActive);

    PaymentConditionEntity getPaymentConditionEntityByCode(String code);

    PaymentConditionEntity getPaymentConditionEntityByLabel(String label);

    boolean existsByCode(String code);

    boolean existsByLabelAndIdPaymentConditionNot(String label, UUID idPaymentCondition);

    boolean existsByCodeAndIdPaymentConditionNot(String code, UUID idPaymentCondition);

    boolean existsByLabel(String label);
}
