package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.Document;
import com.example.billingservice.infrastructure.out.persistance.dto.StoredDocument;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import com.example.billingservice.domain.enums.DocumentType;

import java.util.UUID;

public interface DocumentStoragePort {
    Document store(String ownerReference, UploadedFile file, DocumentType documentType);
}
