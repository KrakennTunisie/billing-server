package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.AuditLog;
import com.example.billingservice.infrastructure.out.persistance.dto.AuditLogDTO;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepositoryPort {

    public List<AuditLogDTO> findAuditLogsByPartner(UUID idClient);

    public List<AuditLogDTO> findAuditLogsBySupplier(UUID idSupplier);
}
