package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.MailNotificationUseCase;
import com.example.billingservice.application.ports.out.MailNotificationRepositoryPort;
import com.example.billingservice.infrastructure.out.persistance.dto.MailRequest;
import com.example.billingservice.infrastructure.out.persistance.entity.MailNotificationJobEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.MailJobRequestMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailNotificationService implements MailNotificationUseCase {

    private static final int BATCH_SIZE = 50;
    private static final String TOPIC = "billing.mail.events";

    private final MailNotificationRepositoryPort mailJobRepository;
    private final KafkaTemplate<String, MailRequest> kafkaTemplate;
    private final MailJobRequestMapper mailJobRequestMapper;

    @Override
    @Scheduled(fixedDelayString = "${mail.outbox.poll-interval-ms:5000}")
    @Transactional
    public void relayPendingJobs() {
        Pageable page = PageRequest.of(0, BATCH_SIZE);
        List<MailNotificationJobEntity> jobs = mailJobRepository.findJobsToPublish(page);

        if (jobs.isEmpty()) {
            return;
        }

        log.info("Relaying {} mail job(s) to Kafka", jobs.size());

        for (MailNotificationJobEntity job : jobs) {
            try {
                MailRequest event = mailJobRequestMapper.toMailJob(job);

                // eventId as Kafka key -> guarantees ordering per job and lets
                // notification-service dedupe using the same id
                kafkaTemplate.send(TOPIC, String.valueOf(job.getEventId()), event)
                        .whenComplete((result, ex) -> {
                            if (ex != null) {
                                log.error("Failed to publish mail job {}", job.getIdMailJobNotification(), ex);
                                // status stays PENDING/RETRYING -> picked up again next tick
                            }
                        });

                job.markQueued();
            } catch (Exception e) {
                log.error("Error preparing mail job {} for publish", job.getIdMailJobNotification(), e);
                job.markFailed(e.getMessage());
            }
        }

        mailJobRepository.saveAll(jobs);
    }
}
