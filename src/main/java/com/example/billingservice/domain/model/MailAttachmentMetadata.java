package com.example.billingservice.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class MailAttachmentMetadata {

    private UUID id;

    private String fileName;

    private String filePath;

    private MailJob mailJob;
}
