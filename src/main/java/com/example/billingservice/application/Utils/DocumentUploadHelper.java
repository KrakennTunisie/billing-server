package com.example.billingservice.application.Utils;

import com.example.billingservice.application.ports.out.DocumentStoragePort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.exceptions.InvalidDocumentTypeException;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.StoredDocument;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DocumentUploadHelper {
    private final DocumentStoragePort documentStoragePort;


    public DocumentUploadHelper(DocumentStoragePort documentStoragePort) {
        this.documentStoragePort = documentStoragePort;
    }

    public Document uploadAndAttachDocument(
            String ownerReference,
            UploadedFile file,
            DocumentType documentType
    ) {

        Document storedDocument = documentStoragePort.store(ownerReference, file, documentType);

        return storedDocument;
    }


    public void validateCustomerDocumentType(DocumentType documentType) {
        if (documentType != DocumentType.RNE
                && documentType != DocumentType.CONTRACT
                && documentType != DocumentType.PATENT) {
            throw new InvalidDocumentTypeException(
                    "Unsupported customer document type: " + documentType
            );
        }
    }

}
