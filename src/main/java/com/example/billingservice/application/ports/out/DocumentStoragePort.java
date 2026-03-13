package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.enums.UploadedFile;
import com.example.billingservice.domain.model.Document;

import java.util.UUID;

public interface DocumentStroragePort {
    Document store(UUID ownerId, UploadedFile file, DocumentType documentType);
}
