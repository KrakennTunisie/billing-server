package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.AuditLogRepositoryPort;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.AuditLog;
import com.example.billingservice.infrastructure.out.persistance.entity.AuditLogEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.AuditEventMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.AuditLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AuditLogPersistenceAdapter implements AuditLogRepositoryPort {

    private final AuditLogRepository auditLogRepository;
    private final CustomerPersistanceAdapter customerPersistanceAdapter;
    private final SupplierPersistanceAdapter supplierPersistanceAdapter;
    private final AuditEventMapper auditEventMapper;


    @Override
    public List<AuditLog> findAuditLogsByPartner(UUID idClient) {
        try {
            if (!customerPersistanceAdapter.existsByIdPartner(idClient)) {
                throw BillingException.notFound("Client", String.valueOf(idClient));
            }
            List<AuditLogEntity> auditLogEntities = auditLogRepository.findByPartner_IdPartner(idClient);
            return auditLogEntities.stream()
                    .map(entity -> auditEventMapper.toDomain(entity, PartnerType.CLIENT)) // ← passer le type
                    .collect(Collectors.toList());
        } catch (BillingException e) {
            throw e;
        }
    }

    @Override
    public List<AuditLog> findAuditLogsBySupplier(UUID idSupplier) {
        try {
            if (!supplierPersistanceAdapter.existsByIdPartner(idSupplier)) {
                throw BillingException.notFound("Fournisseur", String.valueOf(idSupplier));
            }
            List<AuditLogEntity> auditLogEntities = auditLogRepository.findByPartner_IdPartner(idSupplier);
            return auditLogEntities.stream()
                    .map(entity -> auditEventMapper.toDomain(entity, PartnerType.SUPPLIER))
                    .collect(Collectors.toList());
        } catch (BillingException e) {
            throw e;
        }
    }
}
