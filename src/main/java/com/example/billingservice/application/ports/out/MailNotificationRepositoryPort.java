package com.example.billingservice.application.ports.out;

import com.example.billingservice.infrastructure.out.persistance.dto.MailJobCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobRequest;
import com.example.billingservice.infrastructure.out.persistance.entity.MailNotificationJobEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MailNotificationRepositoryPort {
    List<MailNotificationJobEntity> findJobsToPublish(Pageable pageable);

    void updateStatus(UUID eventId, String status);

    void saveAll(List<MailNotificationJobEntity> entities);

    boolean existsByEventId(UUID eventId);

    MailNotificationJobEntity createMailJob(MailJobRequest mailJobRequest);
}
