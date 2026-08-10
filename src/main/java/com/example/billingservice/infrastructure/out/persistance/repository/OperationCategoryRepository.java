package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.entity.BaseItemOperationCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OperationCategoryRepository extends JpaRepository<BaseItemOperationCategoryEntity, UUID> {

    List<BaseItemOperationCategoryEntity> findAllByIsActive(boolean isActive);

    BaseItemOperationCategoryEntity getBaseItemOperationCategoryEntityByCode(String code);

    BaseItemOperationCategoryEntity getBaseItemOperationCategoryEntityByLabel(String label);

    boolean existsByCode(String code);

    boolean existsByLabel(String label);

    boolean existsByCodeAndIdOperationCategoryNot(String code, UUID idOperationCategory);

    boolean existsByLabelAndIdOperationCategoryNot(String label, UUID idOperationCategory);
}
