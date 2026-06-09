package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.AuditLog;
import com.example.billingservice.infrastructure.out.persistance.dto.AuditLogDTO;

import java.util.List;
import java.util.UUID;

public interface AuditLogUseCase {

     List<AuditLogDTO> findAuditLogsByClient(UUID idClient);
     List<AuditLogDTO> findAuditLogsBySupplier(UUID idSupplier);
}
