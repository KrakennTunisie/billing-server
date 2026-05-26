package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.AuditLog;

import java.util.List;
import java.util.UUID;

public interface AuditLogUseCase {

    public List<AuditLog> findAuditLogsByClient(UUID idClient);
}
