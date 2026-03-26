package com.example.billingservice.application.service;

import com.example.billingservice.application.Utils.DocumentUploadHelper;
import com.example.billingservice.application.ports.in.UploadDocumentUseCase;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.model.Document;
import org.springframework.stereotype.Service;


@Service
public class UploadDocumentService implements UploadDocumentUseCase {
    private final DocumentUploadHelper documentUploadHelper;

    public UploadDocumentService(DocumentUploadHelper documentUploadHelper) {
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
