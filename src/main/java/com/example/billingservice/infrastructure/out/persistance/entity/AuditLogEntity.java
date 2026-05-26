package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.AuditEventTrigger;
import com.example.billingservice.domain.enums.AuditType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idAuditLog;

    private String entityName;

    private UUID   entityId;

    @Enumerated(EnumType.STRING)
    private AuditType auditEventType;

    @Enumerated(EnumType.STRING)
    private AuditEventTrigger auditEventTrigger;

    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;

    @Column(length = 1000)
    private String description;

    private String triggeredBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id" , nullable = true)
    private PartnerEntity partner;
}
