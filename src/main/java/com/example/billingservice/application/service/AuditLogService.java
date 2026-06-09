package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.AuditLogUseCase;
import com.example.billingservice.application.ports.out.AuditLogRepositoryPort;
import com.example.billingservice.domain.model.AuditLog;
import com.example.billingservice.infrastructure.out.persistance.dto.AuditLogDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuditLogService implements AuditLogUseCase {
    private final AuditLogRepositoryPort auditLogRepositoryPort;
    @Override
    public List<AuditLogDTO> findAuditLogsByClient(UUID idClient) {
        return auditLogRepositoryPort.findAuditLogsByPartner(idClient);
    }

    @Override
    public List<AuditLogDTO> findAuditLogsBySupplier(UUID idSupplier) {
        return auditLogRepositoryPort.findAuditLogsBySupplier(idSupplier);
    }
}
