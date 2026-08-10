package com.example.billingservice.infrastructure.out.persistance.repository;

import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCreditNoteEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.MailJobEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MailJobRepository extends JpaRepository<MailJobEntity, UUID> {

    @Query("""
        SELECT cn FROM MailJobEntity cn
        WHERE
            cn.toEmail = :email
        """)
    Page<MailJobEntity> getMailJobByPartner(
            @Param("email") String email,
            Pageable pageable
    );

    boolean existsMailJobEntityById(UUID id);

    boolean existsByEventId(String eventId);

    MailJobEntity findByEventId(String eventId);
}
