package com.example.billingservice.application.service;

import com.example.billingservice.application.Utils.DocumentUploadHelper;
import com.example.billingservice.application.ports.in.GetDocumentUseCase;
import com.example.billingservice.application.ports.in.UploadPartnerDocumentUseCase;
import com.example.billingservice.application.ports.out.DocumentReaderPort;
import com.example.billingservice.domain.model.DocumentContent;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import com.example.billingservice.application.ports.out.CustomerRepositoryPort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.exceptions.CustomerNotFoundException;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.Partner;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UploadCustomerDocumentService implements UploadPartnerDocumentUseCase {

    private final DocumentUploadHelper documentUploadHelper;

    public UploadCustomerDocumentService(DocumentUploadHelper documentUploadHelper) {
        this.documentUploadHelper = documentUploadHelper;
    }

    @Override
    public Document upload(String ownerReference, DocumentType documentType, UploadedFile file) {
        documentUploadHelper.validateCustomerDocumentType(documentType);

        Document uploadedDocument = documentUploadHelper
                .uploadAndAttachDocument( ownerReference, file, documentType);

        return uploadedDocument;
    }


}
