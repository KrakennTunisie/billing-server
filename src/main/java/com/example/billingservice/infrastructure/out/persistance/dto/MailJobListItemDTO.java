package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.MailJobStatus;
import com.example.billingservice.domain.model.MailAttachmentMetadata;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class MailJobListItemDTO {
    private UUID idMailJob;
    private String to;
    private String subject;
    private MailJobStatus status;
    private LocalDateTime date;
    private List<MailAttachmentMetadata> attachments;
}
