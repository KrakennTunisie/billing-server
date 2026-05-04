package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.DocumentContent;
import com.example.billingservice.infrastructure.out.persistance.dto.DocumentReadFile;

import java.util.UUID;

public interface GetDocumentUseCase {

    DocumentContent getDocument(String idDocument);

    DocumentReadFile getFileAttachment(UUID idDocument);
}
