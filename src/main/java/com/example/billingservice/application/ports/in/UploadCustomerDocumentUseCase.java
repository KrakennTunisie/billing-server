package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.enums.UploadedFile;
import com.example.billingservice.domain.model.Document;

import java.util.UUID;

public interface UploadPartnerDocumentUseCase {
    Document upload(UUID partnerId, DocumentType documentType, UploadedFile file);

}
