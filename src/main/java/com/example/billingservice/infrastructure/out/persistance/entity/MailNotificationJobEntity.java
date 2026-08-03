package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.MailEventType;
import com.example.billingservice.domain.enums.NotificationJobStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "mail_notification_jobs", indexes = {
        @Index(name = "idx_notification_jobs_status", columnList = "status"),
        @Index(name = "idx_notification_jobs_created_at", columnList = "createdAt")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailNotificationJobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idMailJobNotification;

    // ===== Contenu du mail =====
    @Column(nullable = false)
    private String toEmail;

    @Column(nullable = false)
    private String subject;

    @Column(length = 5000)
    private String body;

    // ===== Traçabilité métier (pour idempotence côté consumer + debug) =====
    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private MailEventType eventType; // ex: "INVOICE_CREATED", "PASSWORD_RESET_OTP"

    @Column
    private String aggregateId; // ex: invoiceId, userId — l'entité métier à l'origine du mail

    // ===== Outbox lifecycle =====
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private NotificationJobStatus status = NotificationJobStatus.PENDING;

    @Column(nullable = false)
    @Builder.Default
    private int retryCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private int maxRetries = 3;

    @Column
    private String lastError;

    @Column
    private LocalDateTime lastAttemptAt;

    @Column
    private LocalDateTime sentAt;

    // ===== Timestamps techniques =====
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ===== Idempotence côté publication Kafka =====
    // Utilisé comme eventId/messageKey unique publié vers mail-service.
    // Si le relay crashe après publish mais avant update status,
    // ce champ garantit qu'on ne republie jamais le même eventId deux fois côté consumer.
    @Column(nullable = false, unique = true, updatable = false)
    @Builder.Default
    private UUID eventId = UUID.randomUUID();

    // ===== Pièces jointes =====
    @OneToMany(mappedBy = "mailNotificationJob", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MailNotificationAttachmentEntity> attachments = new ArrayList<>();

    // ===== Helpers métier =====
    public void markQueued() {
        this.status = NotificationJobStatus.QUEUED;
        this.lastAttemptAt = LocalDateTime.now();
    }

    public void markSent() {
        this.status = NotificationJobStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    public void markFailed(String error) {
        this.retryCount++;
        this.lastError = error;
        this.lastAttemptAt = LocalDateTime.now();
        this.status = (retryCount >= maxRetries) ? NotificationJobStatus.FAILED : NotificationJobStatus.RETRYING;
    }

    public boolean canRetry() {
        return retryCount < maxRetries;
    }
}
