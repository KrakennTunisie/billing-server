package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.DocumentContent;

public interface GetDocumentUseCase {

    DocumentContent getDocument(String idDocument);
}
