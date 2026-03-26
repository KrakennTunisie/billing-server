package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;

public interface UploadDocumentUseCase {
    Document upload(String ownerReference, DocumentType documentType, UploadedFile file);

}
