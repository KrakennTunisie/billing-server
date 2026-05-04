package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.GetDocumentUseCase;
import com.example.billingservice.application.ports.out.DocumentReaderPort;
import com.example.billingservice.domain.model.DocumentContent;
import com.example.billingservice.infrastructure.out.persistance.dto.DocumentReadFile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReadDocumentService implements GetDocumentUseCase {
    private final DocumentReaderPort documentReaderPort;

    public ReadDocumentService(DocumentReaderPort documentReaderPort) {
        this.documentReaderPort = documentReaderPort;
    }


    @Override
    public DocumentContent getDocument(String idDocument) {
        return documentReaderPort.read(idDocument);
    }

    @Override
    public DocumentReadFile getFileAttachment(UUID idDocument) {
        return documentReaderPort.getFileAttachment(idDocument);
    }
}
