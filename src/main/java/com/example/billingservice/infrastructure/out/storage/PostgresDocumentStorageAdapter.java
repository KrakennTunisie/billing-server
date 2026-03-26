package com.example.billingservice.infrastructure.out.storage;

import com.example.billingservice.domain.enums.DocumentStorageMode;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.DocumentContent;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import com.example.billingservice.application.ports.out.DocumentStoragePort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.exceptions.DocumentStorageException;
import com.example.billingservice.infrastructure.out.persistance.entity.DocumentContentEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.DocumentContentMapper;
import com.example.billingservice.infrastructure.out.persistance.mapper.DocumentMapper;
import com.example.billingservice.shared.DocumentUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "spring.storage.type", havingValue = "database")
public class PostgresDocumentStorageAdapter implements DocumentStoragePort {

    private final String apiBaseUrl;
    private final String downloadPrefix;
    private final DocumentMapper documentMapper;
    private final DocumentContentMapper documentContentMapper;

    public PostgresDocumentStorageAdapter(
            @Value("${spring.storage.local.public-base-url}") String apiBaseUrl,
            @Value("${spring.storage.database.download-path}") String downloadPrefix,
            DocumentMapper documentMapper,
            DocumentContentMapper documentContentMapper
    ) {
        this.apiBaseUrl = apiBaseUrl;
        this.downloadPrefix = downloadPrefix;
        this.documentMapper = documentMapper;
        this.documentContentMapper = documentContentMapper;
    }

    @Override
    public Document store(String ownerReference, UploadedFile file, DocumentType documentType) {
        DocumentUtils.validateFile(file);

        try {
            Document document = documentMapper.fromUploadedFile(file, documentType);
            document.setStorageMode(DocumentStorageMode.DATABASE);

            DocumentContentEntity documentContentEntity = DocumentContentEntity.builder()
                    .idDocument(UUID.randomUUID())
                    .fileName(file.originalFileName())
                    .mimeType(file.mimeType())
                    .fileContent(file.content())
                    .fileSize((long) file.content().length)
                    .build();

            DocumentContent documentContent = documentContentMapper.toDomain(documentContentEntity);
            String downloadUrl = apiBaseUrl + downloadPrefix + documentContent.getIdDocumentContent()+ "/content";
            document.setStorageURL(downloadUrl);
            document.setContent(documentContent);
            return document;

        } catch (Exception e) {
            throw new DocumentStorageException("Failed to store file in PostgreSQL",e);
        }
    }


}
