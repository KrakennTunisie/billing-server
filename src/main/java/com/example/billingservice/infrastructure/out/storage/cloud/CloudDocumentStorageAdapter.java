package com.example.billingservice.infrastructure.out.storage.cloud;

import com.example.billingservice.infrastructure.out.persistance.dto.CloudStoredObject;
import com.example.billingservice.infrastructure.out.persistance.dto.StoredDocument;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import com.example.billingservice.application.ports.out.DocumentStoragePort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.exceptions.DocumentStorageException;
import com.example.billingservice.shared.DocumentUtils;
import com.example.billingservice.shared.HashUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "cloud")
public class CloudDocumentStorageAdapter implements DocumentStoragePort {
    private final CloudObjectStorageClient cloudObjectStorageClient;

    public CloudDocumentStorageAdapter(CloudObjectStorageClient cloudObjectStorageClient) {
        this.cloudObjectStorageClient = cloudObjectStorageClient;
    }

    @Override
    public StoredDocument store(UUID ownerId, UploadedFile file, DocumentType documentType) {
        DocumentUtils.validateFile(file);

        try {
            String objectKey = buildObjectKey(ownerId, documentType, file.originalFileName());

            CloudStoredObject storedObject = cloudObjectStorageClient.upload(
                    objectKey,
                    file.content(),
                    file.mimeType()
            );

            return new StoredDocument(
                    file.originalFileName(),
                    file.mimeType(),
                    storedObject.publicUrl(),
                    HashUtils.sha256(file.content())
            );

        } catch (Exception e) {
            throw new DocumentStorageException("Failed to store file in cloud storage", e);
        }
    }


    private String buildObjectKey(UUID ownerId, DocumentType documentType, String originalFileName) {
        String rootFolder = DocumentUtils.resolveOwnerFolder(documentType);
        String safeFileName = DocumentUtils.sanitizeFileName(originalFileName);

        return rootFolder + "/"
                + ownerId + "/"
                + documentType.name().toLowerCase() + "/"
                + UUID.randomUUID() + "_"
                + safeFileName;
    }




}