package com.example.billingservice.application.service;

import com.example.billingservice.application.Utils.DocumentUploadHelper;
import com.example.billingservice.application.ports.in.UploadPartnerDocumentUseCase;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import com.example.billingservice.application.ports.out.SupplierRepositoryPort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.exceptions.SupplierNotFoundException;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.Partner;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UploadSupplierDocumentService implements UploadPartnerDocumentUseCase {
    private final SupplierRepositoryPort supplierRepositoryPort;
    private final DocumentUploadHelper documentUploadHelper;

    public UploadSupplierDocumentService(
            SupplierRepositoryPort supplierRepositoryPort, DocumentUploadHelper documentUploadHelper
    ) {
        this.supplierRepositoryPort = supplierRepositoryPort;
        this.documentUploadHelper = documentUploadHelper;
    }

    @Override
    public Document upload(UUID customerId, DocumentType documentType, UploadedFile file) {
        DocumentUploadHelper.validateCustomerDocumentType(documentType);

        Partner supplier = supplierRepositoryPort.findSupplierById(customerId.toString())
                .orElseThrow(() -> new SupplierNotFoundException(customerId));

        Document uploadedDocument = documentUploadHelper
                .uploadAndAttachDocument( customerId, file, documentType);

        Partner updatedSupplier = DocumentUploadHelper.attachDocument(supplier, uploadedDocument);

        supplierRepositoryPort.updateSupplier(updatedSupplier);

        return uploadedDocument;

    }




}
