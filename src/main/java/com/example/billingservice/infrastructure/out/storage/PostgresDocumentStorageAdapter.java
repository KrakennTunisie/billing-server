package com.example.billingservice.infrastructure.out.storage;

import com.example.billingservice.domain.model.Document;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import com.example.billingservice.application.ports.out.DocumentStoragePort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.exceptions.DocumentStorageException;
import com.example.billingservice.infrastructure.out.persistance.entity.DocumentEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.DocumentMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.JpaDocumentRepository;
import com.example.billingservice.shared.DocumentUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "spring.storage.type", havingValue = "postgres")
public class PostgresDocumentStorageAdapter implements DocumentStoragePort {

    private final JpaDocumentRepository documentContentJpaRepository;
    private final String apiBaseUrl;
    private final DocumentMapper documentMapper;

    public PostgresDocumentStorageAdapter(
            JpaDocumentRepository documentContentJpaRepository,
            @Value("${spring.storage.local.public-base-url}") String apiBaseUrl, DocumentMapper documentMapper
    ) {
        this.documentContentJpaRepository = documentContentJpaRepository;
        this.apiBaseUrl = apiBaseUrl;
        this.documentMapper = documentMapper;
    }

    @Override
    public Document store(String ownerReference, UploadedFile file, DocumentType documentType) {
        DocumentUtils.validateFile(file);

        try {

            DocumentEntity documentEntity = documentContentJpaRepository.save(new DocumentEntity());

            String downloadUrl = apiBaseUrl + "/api/documents/" + documentEntity.getIdDocument()+ "/download";

            return documentMapper.toDomain(documentEntity);

        } catch (Exception e) {
            throw new DocumentStorageException("Failed to store file in PostgreSQL",e);
        }
    }


}
