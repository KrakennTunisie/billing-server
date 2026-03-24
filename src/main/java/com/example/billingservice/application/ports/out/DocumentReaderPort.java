package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.DocumentContent;

public interface DocumentReaderPort {
    DocumentContent read(String idDocument);
}
