package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.entity.MailNotificationJobEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MailNotificationRepository extends JpaRepository<MailNotificationJobEntity, UUID> {
    @Query("""
        SELECT m FROM MailNotificationJobEntity m
        WHERE m.status IN (com.example.billingservice.domain.enums.NotificationJobStatus.PENDING,
                            com.example.billingservice.domain.enums.NotificationJobStatus.RETRYING)
        ORDER BY m.createdAt ASC
        """)
    List<MailNotificationJobEntity> findJobsToPublish(Pageable pageable);

    boolean existsByEventId(UUID eventId);

    MailNotificationJobEntity findByEventId(UUID eventId);
}
