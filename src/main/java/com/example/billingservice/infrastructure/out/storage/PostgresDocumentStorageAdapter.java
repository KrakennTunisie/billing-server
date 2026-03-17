package com.example.billingservice.infrastructure.out.storage;

import com.example.billingservice.infrastructure.out.persistance.dto.StoredDocument;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import com.example.billingservice.application.ports.out.DocumentStoragePort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.exceptions.DocumentStorageException;
import com.example.billingservice.infrastructure.out.persistance.entity.DocumentContentEntity;
import com.example.billingservice.infrastructure.out.persistance.repository.DocumentContentJpaRepository;
import com.example.billingservice.shared.DocumentUtils;
import com.example.billingservice.shared.HashUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "spring.storage.type", havingValue = "postgres")
public class PostgresDocumentStorageAdapter implements DocumentStoragePort {

    private final DocumentContentJpaRepository documentContentJpaRepository;
    private final String apiBaseUrl;

    public PostgresDocumentStorageAdapter(
            DocumentContentJpaRepository documentContentJpaRepository,
            @Value("${spring.storage.local.public-base-url}") String apiBaseUrl
    ) {
        this.documentContentJpaRepository = documentContentJpaRepository;
        this.apiBaseUrl = apiBaseUrl;
    }

    @Override
    public StoredDocument store(UUID ownerId, UploadedFile file, DocumentType documentType) {
        DocumentUtils.validateFile(file);

        try {
            UUID contentId = UUID.randomUUID();

            DocumentContentEntity entity = new DocumentContentEntity();
            entity.setId(contentId);
            entity.setOriginalFileName(file.originalFileName());
            entity.setMimeType(file.mimeType());
            entity.setFileData(file.content());

            documentContentJpaRepository.save(entity);

            String downloadUrl = apiBaseUrl + "/api/documents/" + contentId + "/download";

            return new StoredDocument(
                    file.originalFileName(),
                    file.mimeType(),
                    downloadUrl,
                    HashUtils.sha256(file.content())
            );

        } catch (Exception e) {
            throw new DocumentStorageException("Failed to store file in PostgreSQL",e);
        }
    }


}
