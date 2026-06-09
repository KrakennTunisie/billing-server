package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.AuditEventTrigger;
import com.example.billingservice.domain.enums.AuditType;
import com.example.billingservice.domain.model.Partner;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder
public class AuditLogDTO {

    private UUID idLog;
    private String entityName;
    private UUID   entityId;
    private AuditType AuditLogType;
    private Date LogDate;
    private String description;
    private AuditEventTrigger auditEventTrigger;
    private String triggeredBy;
    private PartnerSummaryDTO customer;
}
