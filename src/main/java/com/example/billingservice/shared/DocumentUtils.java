package com.example.billingservice.shared;

import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;

public final class DocumentUtils {
    public DocumentUtils() {
    }

    public static void validateFile(UploadedFile file) {
        if (file == null) {
            throw new IllegalArgumentException("Uploaded file must not be null");
        }
        if (file.content() == null || file.content().length == 0) {
            throw new IllegalArgumentException("Uploaded file content must not be empty");
        }
        if (file.originalFileName() == null || file.originalFileName().isBlank()) {
            throw new IllegalArgumentException("Uploaded file name must not be blank");
        }
    }

    public static String resolveOwnerFolder(DocumentType documentType) {
        return documentType == DocumentType.INVOICE ? "invoices" : "partners";
    }


    public static String extractExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return index >= 0 ? fileName.substring(index) : "";
    }

    public static String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
