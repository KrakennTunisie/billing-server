package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.MailNotificationRepositoryPort;
import com.example.billingservice.domain.enums.NotificationJobStatus;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobRequest;
import com.example.billingservice.infrastructure.out.persistance.entity.MailNotificationJobEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.MailJobRequestMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.MailNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MailNotificationRepositoryAdapter implements MailNotificationRepositoryPort {

    private final MailNotificationRepository mailNotificationRepository;
    private final MailJobRequestMapper mailJobRequestMapper;
    @Override
    public List<MailNotificationJobEntity> findJobsToPublish(Pageable pageable) {
        return mailNotificationRepository.findJobsToPublish(pageable);
    }

    @Override
    public void updateStatus(UUID eventId, String status) {

        MailNotificationJobEntity mailNotificationJob =  mailNotificationRepository.findByEventId(eventId);

        mailNotificationJob.setStatus(NotificationJobStatus.valueOf(status));

        mailNotificationRepository.save(mailNotificationJob);
    }

    @Override
    public void saveAll(List<MailNotificationJobEntity> entities) {
        mailNotificationRepository.saveAll(entities);
    }

    @Override
    public boolean existsByEventId(UUID eventId) {
        return mailNotificationRepository.existsByEventId(eventId);
    }

    @Override
    public MailNotificationJobEntity createMailJob(MailJobRequest mailJobRequest) {

        MailNotificationJobEntity mailNotificationJob = mailJobRequestMapper.toNotificationEntity(mailJobRequest);

       return mailNotificationRepository.save(mailNotificationJob);

    }
}
