package com.example.billingservice.application.service;

import com.example.billingservice.application.Utils.DocumentUploadHelper;
import com.example.billingservice.application.ports.in.UploadPartnerDocumentUseCase;
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

    private final CustomerRepositoryPort customerRepositoryPort;
    private final DocumentUploadHelper documentUploadHelper;

    public UploadCustomerDocumentService(
            CustomerRepositoryPort customerRepositoryPort, DocumentUploadHelper documentUploadHelper
    ) {
        this.customerRepositoryPort = customerRepositoryPort;
        this.documentUploadHelper = documentUploadHelper;
    }

    @Override
    public Document upload(UUID customerId, DocumentType documentType, UploadedFile file) {
        documentUploadHelper.validateCustomerDocumentType(documentType);

        Partner customer = customerRepositoryPort.findCustomerById(customerId.toString())
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        Document uploadedDocument = documentUploadHelper
                .uploadAndAttachDocument( customerId, file, documentType);

        Partner updatedCustomer = documentUploadHelper.attachDocument(customer, uploadedDocument);

        customerRepositoryPort.updateCustomer(updatedCustomer);

        return uploadedDocument;
    }



}
