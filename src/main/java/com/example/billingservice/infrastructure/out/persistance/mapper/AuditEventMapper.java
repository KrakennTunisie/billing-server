package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.model.AuditLog;
import com.example.billingservice.infrastructure.out.persistance.dto.AuditLogDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.AuditLogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditEventMapper {

      private final PartnerMapper  partnerMapper;
    // =========================
    // ENTITY → DOMAIN
    // =========================
    public AuditLog toDomain(AuditLogEntity entity, PartnerType type) {
        if (entity == null) {
            return null;
        }

        AuditLog auditLog = AuditLog.builder()
                .idLog(entity.getIdAuditLog())
                .AuditLogType(entity.getAuditEventType())
                .auditEventTrigger(entity.getAuditEventTrigger())
                .triggeredBy(entity.getTriggeredBy())
                .entityId(entity.getEntityId())
                .entityName(entity.getEntityName())
                .description(entity.getDescription())
                .LogDate(entity.getEventDate())
                .customer(partnerMapper.toDomain(entity.getPartner(),type))
                .build();

        return auditLog;
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public AuditLogEntity toEntity(AuditLog domain) {
        if (domain == null) {
            return null;
        }

        AuditLogEntity entity = new AuditLogEntity();
        entity.setIdAuditLog(domain.getIdLog());
        entity.setEntityId(domain.getEntityId());
        entity.setEntityName(domain.getEntityName());
        entity.setDescription(domain.getDescription());
        entity.setEventDate(domain.getLogDate());
        entity.setAuditEventTrigger(domain.getAuditEventTrigger());
        entity.setAuditEventType(domain.getAuditLogType());
        entity.setTriggeredBy(domain.getTriggeredBy());
        entity.setPartner(partnerMapper.toEntity(domain.getCustomer()));
        return entity;
    }

    public AuditLogDTO toDTO(AuditLog domain){
        if (domain == null) {
            return null;
        }

        return AuditLogDTO.builder()
                .idLog(domain.getIdLog())
                .entityName(domain.getEntityName())
                .entityId(domain.getEntityId())
                .AuditLogType(domain.getAuditLogType())
                .LogDate(domain.getLogDate())
                .description(domain.getDescription())
                .auditEventTrigger(domain.getAuditEventTrigger())
                .triggeredBy(domain.getTriggeredBy())
                .customer(partnerMapper.toSummaryDTO(domain.getCustomer()))
                .build();
    }
}
