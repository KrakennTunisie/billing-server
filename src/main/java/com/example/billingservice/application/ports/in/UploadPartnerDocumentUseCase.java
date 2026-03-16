package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;

import java.util.UUID;

public interface UploadPartnerDocumentService {
    Document upload(UUID customerId, DocumentType documentType, UploadedFile file);

}
