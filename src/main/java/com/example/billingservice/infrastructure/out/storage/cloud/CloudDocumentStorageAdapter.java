package com.example.billingservice.infrastructure.out.storage;

import com.example.billingservice.infrastructure.out.persistance.dto.StoredDocument;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import com.example.billingservice.application.ports.out.DocumentStoragePort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.exceptions.DocumentStorageException;
import com.example.billingservice.shared.HashUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "cloud")
public class CloudDocumentStorageAdapter implements DocumentStoragePort {

    @Override
    public StoredDocument store(UUID ownerId, UploadedFile file, DocumentType documentType) {
        validateFile(file);

        try {
            String ownerFolder = resolveOwnerFolder(documentType);

            String objectKey = ownerFolder + "/"
                    + ownerId + "/"
                    + documentType.name().toLowerCase() + "/"
                    + UUID.randomUUID() + "_"
                    + sanitizeFileName(file.originalFileName());

            /*
             * Example for S3 / MinIO / Azure / GCS:
             *
             * cloudClient.upload(objectKey, file.content(), file.mimeType());
             */

            String publicUrl = buildPublicUrl(objectKey);

            return new StoredDocument(
                    file.originalFileName(),
                    file.mimeType(),
                    publicUrl,
                    HashUtils.sha256(file.content())
            );

        } catch (Exception e) {
            throw new DocumentStorageException("Failed to store file in cloud storage", e);
        }
    }

    private void validateFile(UploadedFile file) {
        if (file == null) {
            throw new IllegalArgumentException("Uploaded file must not be null");
        }
        if (file.content() == null || file.content().length == 0) {
            throw new IllegalArgumentException("Uploaded file content must not be empty");
        }
        if (file.originalFileName() == null || file.originalFileName().isBlank()) {
            throw new IllegalArgumentException("Uploaded file name must not be blank");
        }
    }

    private String resolveOwnerFolder(DocumentType documentType) {
        return documentType == DocumentType.INVOICE ? "invoices" : "partners";
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String buildPublicUrl(String objectKey) {
        return "https://your-cloud-storage.example.com/" + objectKey;
    }
}