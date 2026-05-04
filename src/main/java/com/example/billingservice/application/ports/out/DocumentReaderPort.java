package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.DocumentContent;
import com.example.billingservice.infrastructure.out.persistance.dto.DocumentReadFile;

import java.util.UUID;

public interface DocumentReaderPort {
    DocumentContent read(String idDocument);

    DocumentReadFile getFileAttachment(UUID idDocument);

}
