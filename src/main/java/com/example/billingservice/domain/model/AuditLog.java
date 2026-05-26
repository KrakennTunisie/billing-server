package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.AuditEventTrigger;
import com.example.billingservice.domain.enums.AuditType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Modèle Audit Log")
public class AuditLog {

    private UUID idLog;
    private String entityName;
    private UUID   entityId;
    private AuditType AuditLogType;
    private Date LogDate;
    private String description;
    private AuditEventTrigger auditEventTrigger;
    private String triggeredBy;
    private Partner customer;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuditLog auditLog = (AuditLog) o;
        return AuditLogType == auditLog.AuditLogType && Objects.equals(description, auditLog.description) && auditEventTrigger == auditLog.auditEventTrigger;
    }

    @Override
    public int hashCode() {
        return Objects.hash(AuditLogType, description, auditEventTrigger);
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "idLog=" + idLog +
                ", entityName='" + entityName + '\'' +
                ", entityId=" + entityId +
                ", AuditLogType=" + AuditLogType +
                ", LogDate=" + LogDate +
                ", description='" + description + '\'' +
                ", eventTrigger=" + auditEventTrigger +
                ", triggeredBy='" + triggeredBy + '\'' +
                '}';
    }
}
