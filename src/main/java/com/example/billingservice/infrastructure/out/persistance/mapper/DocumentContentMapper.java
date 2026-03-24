package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.model.DocumentContent;
import com.example.billingservice.infrastructure.out.persistance.entity.DocumentContentEntity;
import org.springframework.stereotype.Component;

@Component
public class DocumentContentMapper {

    /**
     * Entity → Domain
     */
    public DocumentContent toDomain(DocumentContentEntity entity) {
        if (entity == null) {
            return null;
        }

        return DocumentContent.builder()
                .idDocumentContent(entity.getIdDocument())
                .fileName(entity.getFileName())
                .mimeType(entity.getMimeType())
                .fileSize(entity.getFileSize())
                .fileContent(entity.getFileContent())
                .build();
    }

    /**
     * Domain → Entity
     */
    public DocumentContentEntity toEntity(DocumentContent domain) {
        if (domain == null) {
            return null;
        }

        return DocumentContentEntity.builder()
                .idDocument(domain.getIdDocumentContent())
                .fileName(domain.getFileName())
                .mimeType(domain.getMimeType())
                .fileSize(domain.getFileSize())
                .fileContent(domain.getFileContent())
                .build();
    }

}
