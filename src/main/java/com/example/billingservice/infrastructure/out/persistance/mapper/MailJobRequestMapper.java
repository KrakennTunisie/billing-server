package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.MailJobStatus;
import com.example.billingservice.domain.model.MailAttachmentMetadata;
import com.example.billingservice.infrastructure.out.persistance.dto.MailAttachementMetadata;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobAttachmentRequest;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobRequest;
import com.example.billingservice.infrastructure.out.persistance.dto.MailRequest;
import com.example.billingservice.infrastructure.out.persistance.entity.MailAttachmentMetadataEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.MailJobEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.MailNotificationAttachmentEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.MailNotificationJobEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MailJobRequestMapper {

    /**
     * MailJobAttachmentRequest -> MailAttachmentMetadataEntity
     */
    public MailAttachmentMetadataEntity toAttachmentEntity(
            MailJobAttachmentRequest request
    ) {
        if (request == null) {
            return null;
        }

        MailAttachmentMetadataEntity entity =
                new MailAttachmentMetadataEntity();

        entity.setFileName(request.fileName());
        entity.setFilePath(request.filePath());

        return entity;
    }


    /**
     * MailJobRequest -> MailJobEntity
     */
    public MailJobEntity toEntity(MailJobRequest request, String eventId) {

        if (request == null) {
            return null;
        }

        MailJobEntity entity = new MailJobEntity();

        entity.setToEmail(request.toEmail());
        entity.setSubject(request.subject());
        entity.setBody(request.body());
        entity.setEventId(eventId);
        entity.setDate(LocalDateTime.now());
        entity.setStatus(MailJobStatus.CREATED);

        if (request.attachments() != null) {

            List<MailAttachmentMetadataEntity> attachments =
                    request.attachments()
                            .stream()
                            .map(this::toAttachmentEntity)
                            .toList();

            // Important : relation bidirectionnelle
            attachments.forEach(
                    attachment -> attachment.setMailJob(entity)
            );

            entity.setAttachments(
                    new ArrayList<>(attachments)
            );
        }

        return entity;
    }


    /**
     * MailJobRequest -> MailNotificationJobEntity
     */
    public MailNotificationJobEntity toNotificationEntity(
            MailJobRequest request
    ) {

        if (request == null) {
            return null;
        }

        MailNotificationJobEntity entity =
                new MailNotificationJobEntity();

        entity.setToEmail(request.toEmail());
        entity.setSubject(request.subject());
        entity.setBody(request.body());
        entity.setEventType(request.eventType());

        // Les valeurs par défaut sont normalement déjà définies
        // dans l'entité via @Builder.Default / initialisation.
       // entity.setStatus(NotificationJobStatus.PENDING);
      //  entity.setRetryCount(0);
       // entity.setMaxRetries(3);
        entity.setEventId(UUID.randomUUID());

        // Si eventId est généré par le champ de l'entité,
        // cette ligne peut être supprimée.

        if (request.attachments() != null) {

            List<MailNotificationAttachmentEntity> attachments =
                    request.attachments()
                            .stream()
                            .map(e->toNotificationAttachmentEntity(e,entity ))
                            .toList();


            entity.setAttachments(
                    new ArrayList<>(attachments)
            );
        }

        return entity;
    }


    /**
     * MailJobAttachmentRequest
     * -> MailNotificationAttachmentEntity
     */
    public MailNotificationAttachmentEntity toNotificationAttachmentEntity(
            MailJobAttachmentRequest request, MailNotificationJobEntity mailNotificationJob
    ) {

        if (request == null) {
            return null;
        }

        MailNotificationAttachmentEntity entity =
                new MailNotificationAttachmentEntity();

        entity.setIdDocument(String.valueOf(request.attachmentRequestId()));

        entity.setFileName(request.fileName());
        entity.setFilePath(request.filePath());
        entity.setMailNotificationJob(mailNotificationJob);

        return entity;
    }



    public MailRequest toMailJob(MailNotificationJobEntity entity) {

        if (entity == null) {
            return null;
        }

        List<MailAttachementMetadata> attachments =
                entity.getAttachments() == null
                        ? List.of()
                        : entity.getAttachments()
                        .stream()
                        .map(this::toMailAttachmentMetadata)
                        .toList();

        return MailRequest.builder()
                .eventId(entity.getEventId() != null
                        ? entity.getEventId().toString()
                        : null)
                .eventType(entity.getEventType() != null
                        ? entity.getEventType().name()
                        : null)
                .toEmail(entity.getToEmail())
                .subject(entity.getSubject())
                .body(entity.getBody())
                .attachments(attachments)
                .occurredAt(entity.getCreatedAt())
                .build();
    }

    private MailAttachementMetadata toMailAttachmentMetadata(
            MailNotificationAttachmentEntity entity
    ) {

        if (entity == null) {
            return null;
        }

        return new MailAttachementMetadata(
                entity.getId(),
                entity.getIdDocument(),
                entity.getFileName(),
                entity.getFilePath(),
                ""
        );
    }



}
