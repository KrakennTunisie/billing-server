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
            UUID ownerId,
            UploadedFile file,
            DocumentType documentType
    ) {

        StoredDocument storedDocument = documentStoragePort.store(ownerId, file, documentType);

        return buildDocument(storedDocument, documentType);
    }

    private Document buildDocument(StoredDocument storedDocument, DocumentType documentType) {
        return Document.builder()
                .idDocument(UUID.randomUUID())
                .fileName(storedDocument.fileName())
                .mimeType(storedDocument.mimeType())
                .storageURL(storedDocument.storageUrl())
                .hash(storedDocument.hash())
                .uploadedAt(LocalDateTime.now())
                .documentType(documentType)
                .build();
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

    public Partner attachDocument(Partner customer, Document document) {
        return Partner.builder()
                .idPartner(customer.getIdPartner())
                .name(customer.getName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .taxRegistrationNumber(customer.getTaxRegistrationNumber())
                .country(customer.getCountry())
                .address(customer.getAddress())
                .iban(customer.getIban())
                .partnerType(customer.getPartnerType())
                .invoices(customer.getInvoices())
                .rne(document.getDocumentType() == DocumentType.RNE ? document : customer.getRne())
                .contract(document.getDocumentType() == DocumentType.CONTRACT ? document : customer.getContract())
                .patente(document.getDocumentType() == DocumentType.PATENT ? document : customer.getPatente())
                .build();
    }
}
