package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.MailJobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "mail_jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailJobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String eventId;

    @Column(nullable = false)
    private String toEmail;

    @Column(nullable = false)
    private String subject;

    @Column(length = 5000)
    private String body;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MailJobStatus status;

    @OneToMany(mappedBy = "mailJob", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MailAttachmentMetadataEntity> attachments = new ArrayList<>();
}