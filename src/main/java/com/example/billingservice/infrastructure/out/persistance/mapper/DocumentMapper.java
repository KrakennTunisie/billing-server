package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.DocumentStorageMode;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.infrastructure.out.persistance.dto.DocumentResponse;
import com.example.billingservice.infrastructure.out.persistance.dto.DocumentSummaryDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import com.example.billingservice.infrastructure.out.persistance.entity.DocumentEntity;
import com.example.billingservice.shared.HashUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DocumentMapper {

    private final DocumentContentMapper documentContentMapper;
    public Document toDomain(DocumentEntity entity) {
        if (entity == null) {
            return null;
        }

        return Document.builder()
                .idDocument(entity.getIdDocument())
                .fileName(entity.getFileName())
                .mimeType(entity.getMimeType())
                .storageURL(entity.getStorageURL())
                .hash(entity.getHash())
                .uploadedAt(entity.getUploadedAt())
                .documentType(entity.getDocumentType())
                .storageMode(entity.getStorageMode())
                .content(documentContentMapper.toDomain(entity.getContent()))
                .build();
    }

    public DocumentEntity toEntity(Document document, DocumentType documentType) {
        if (document == null) {
            return null;
        }

        DocumentEntity entity = new DocumentEntity();
        entity.setIdDocument(document.getIdDocument());
        entity.setFileName(safe(document.getFileName()));
        entity.setMimeType(safe(document.getMimeType()));
        entity.setHash(safe(document.getHash()));
        entity.setUploadedAt(document.getUploadedAt() != null ? document.getUploadedAt() : LocalDateTime.now());
        entity.setDocumentType(documentType);
        entity.setStorageURL(document.getStorageURL());
        entity.setStorageMode(document.getStorageMode());
        entity.setContent(documentContentMapper.toEntity(document.getContent()));
        return entity;
    }


    public Document fromUploadedFile(UploadedFile uploadedFile, DocumentType documentType) throws IOException {
        if (uploadedFile == null) {
            return null;
        }
        try {

            return Document.builder()
                    .fileName(safe(uploadedFile.originalFileName()))
                    .mimeType(safe(uploadedFile.mimeType()))
                    .uploadedAt(LocalDateTime.now())
                    .documentType(documentType)
                    .hash(HashUtils.sha256(uploadedFile.content().readAllBytes()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



    public DocumentEntity toRneEntity(Document document ) {
        return toEntity(document, DocumentType.RNE);
    }

    public DocumentEntity toPatenteEntity(Document document) {
        return toEntity(document, DocumentType.PATENT);
    }

    public DocumentEntity toContractEntity(Document document) {
        return toEntity(document, DocumentType.CONTRACT);
    }

    public DocumentResponse toResponse(Document document) {
        if (document == null) {
            return null;
        }

        DocumentResponse response = new DocumentResponse();
        response.setId(document.getIdDocument());
        response.setFileName(document.getFileName());
        response.setMimeType(document.getMimeType());
        response.setStorageMode(document.getStorageMode() != null ? document.getStorageMode().name() : null);
        response.setAccessUrl("/api/documents/" + document.getIdDocument());

        if (document.getStorageMode() == DocumentStorageMode.CLOUD_URL) {
            response.setDirectUrl(document.getStorageURL());
        }

        return response;
    }

    public DocumentSummaryDTO toDocumentSummary(Document document){
        if(document==null){
            return null;
        }

        return DocumentSummaryDTO.builder()
                .idDocument(document.getIdDocument())
                .fileName(document.getFileName())
                .mimeType(document.getMimeType())
                .storageURL(document.getStorageURL())
                .hash(document.getHash())
                .storageMode(document.getStorageMode())
                .uploadedAt(document.getUploadedAt())
                .documentType(document.getDocumentType())
                .build();
    }

    private String safe(String value) {
        return value != null ? value : "";
    }
}
