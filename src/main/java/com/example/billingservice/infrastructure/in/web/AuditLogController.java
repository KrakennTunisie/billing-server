package com.example.billingservice.infrastructure.in.web;


import com.example.billingservice.application.ports.in.AuditLogUseCase;
import com.example.billingservice.domain.model.AuditLog;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name= "Logs", description = "Journalisation pour le client")
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogUseCase auditLogUseCase;
    @GetMapping("/logs-clients/{idClient}")
    public ResponseEntity<List<AuditLog>> getClientsLogs(@PathVariable UUID idClient) {
        return ResponseEntity.ok(
                auditLogUseCase.findAuditLogsByClient(idClient)
        );
    }

    @GetMapping("/logs-suppliers/{idSupplier}")
    public ResponseEntity<List<AuditLog>> getSupplierLogs(@PathVariable UUID idSupplier) {
        return ResponseEntity.ok(
                auditLogUseCase.findAuditLogsBySupplier(idSupplier)
        );
    }
}
