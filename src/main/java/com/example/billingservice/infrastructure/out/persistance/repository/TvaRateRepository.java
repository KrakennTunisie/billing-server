package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.entity.TvaRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TvaRateRepository extends JpaRepository<TvaRateEntity, UUID> {

    List<TvaRateEntity> findAllByIsActive(boolean isActive);

    TvaRateEntity getTvaRateEntityByCode(String code);

    TvaRateEntity getTvaRateEntityByLabel(String label);

    boolean existsByCode(String code);

    boolean existsByLabel(String label);

    boolean existsByCodeAndIdTvaRateNot(String code, UUID idTvaRate);

    boolean existsByLabelAndIdTvaRateNot(String label, UUID idTvaRate);
}
