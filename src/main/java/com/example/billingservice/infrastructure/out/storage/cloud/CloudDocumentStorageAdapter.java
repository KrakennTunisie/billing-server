package com.example.billingservice.infrastructure.out.storage.cloud;

import com.example.billingservice.domain.model.Document;
import com.example.billingservice.infrastructure.out.persistance.dto.CloudStoredObject;
import com.example.billingservice.infrastructure.out.persistance.dto.StoredDocument;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import com.example.billingservice.application.ports.out.DocumentStoragePort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.exceptions.DocumentStorageException;
import com.example.billingservice.infrastructure.out.persistance.entity.DocumentEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.DocumentMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.JpaDocumentRepository;
import com.example.billingservice.shared.DocumentUtils;
import com.example.billingservice.shared.HashUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "spring.storage.type", havingValue = "cloud")
public class CloudDocumentStorageAdapter implements DocumentStoragePort {
    private final CloudObjectStorageClient cloudObjectStorageClient;
    private final JpaDocumentRepository jpaDocumentRepository;
    private final DocumentMapper documentMapper;

    public CloudDocumentStorageAdapter(CloudObjectStorageClient cloudObjectStorageClient, JpaDocumentRepository jpaDocumentRepository, DocumentMapper documentMapper) {
        this.cloudObjectStorageClient = cloudObjectStorageClient;
        this.jpaDocumentRepository = jpaDocumentRepository;
        this.documentMapper = documentMapper;
    }

    @Override
    public Document store(String ownerReference, UploadedFile file, DocumentType documentType) {
        DocumentUtils.validateFile(file);

        try {
            String objectKey = buildObjectKey(ownerReference, documentType, file.originalFileName());

            CloudStoredObject storedObject = cloudObjectStorageClient.upload(
                    objectKey,
                    file.content(),
                    file.mimeType()
            );
            DocumentEntity documentEntity = jpaDocumentRepository.save(new DocumentEntity());

            return documentMapper.toDomain(documentEntity);

        } catch (Exception e) {
            throw new DocumentStorageException("Failed to store file in cloud storage", e);
        }
    }


    private String buildObjectKey(String ownerReference, DocumentType documentType, String originalFileName) {
        String rootFolder = DocumentUtils.resolveOwnerFolder(documentType);
        String safeFileName = DocumentUtils.sanitizeFileName(originalFileName);

        return rootFolder + "/"
                + ownerReference + "/"
                + documentType.name().toLowerCase() + "/"
                + UUID.randomUUID() + "_"
                + safeFileName;
    }




}