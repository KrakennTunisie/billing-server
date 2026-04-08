package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.DocumentStorageMode;
import com.example.billingservice.domain.enums.DocumentType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class DocumentSummaryDTO {
    private UUID idDocument;
    private String fileName;
    private String mimeType;
    private String storageURL;
    private String hash;
    private DocumentStorageMode storageMode;
    private LocalDateTime uploadedAt;

    private DocumentType documentType;
}
