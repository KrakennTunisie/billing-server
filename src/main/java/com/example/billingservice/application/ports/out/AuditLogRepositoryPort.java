package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.AuditLog;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepositoryPort {

    public List<AuditLog> findAuditLogsByPartner(UUID idClient);
}
