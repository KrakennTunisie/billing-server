package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.MailJobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MailJobModel {
    private UUID idMailJob;
    private String to;
    private String subject;
    private String body;
    private LocalDateTime date;
    private MailJobStatus status;
    private List<MailAttachmentMetadata> attachments;
}
