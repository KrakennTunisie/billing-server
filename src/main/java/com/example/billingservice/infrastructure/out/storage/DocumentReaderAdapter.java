package com.example.billingservice.infrastructure.out.storage;

import com.example.billingservice.application.ports.out.DocumentReaderPort;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.DocumentContent;
import com.example.billingservice.infrastructure.out.persistance.entity.DocumentContentEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.DocumentContentMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.DocumentContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "spring.storage.type", havingValue = "database")
public class DocumentReaderAdapter implements DocumentReaderPort {
    private final DocumentContentRepository documentContentRepository;
    private final DocumentContentMapper documentContentMapper;

    public DocumentReaderAdapter(DocumentContentRepository documentContentRepository, DocumentContentMapper documentContentMapper) {
        this.documentContentRepository = documentContentRepository;
        this.documentContentMapper = documentContentMapper;
    }

    @Override
    public DocumentContent read(String idDocument) {

        DocumentContentEntity document = documentContentRepository.findById(UUID.fromString(idDocument))
                .orElseThrow(() -> new BillingException(HttpStatus.NOT_FOUND, "404","Not found"));

        return documentContentMapper.toDomain(document);

    }
}
