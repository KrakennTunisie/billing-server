package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.DocumentType;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Document {

    private UUID idDocument;
    private String fileName;
    private String mimeType;
    private String storageURL;
    private String hash;
    private Date uploadedAt;

    private DocumentType documentType;

    public Document(UUID idDocument, String fileName, String mimeType, String storageURL,
                    String hash, Date uploadedAt, DocumentType documentType) {
        this.idDocument = idDocument;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.storageURL = storageURL;
        this.hash = hash;
        this.uploadedAt = uploadedAt;
        this.documentType = documentType;
    }

    public UUID getIdInvoiceDocument() {
        return idDocument;
    }

    public void setIdInvoiceDocument(UUID idDocument) {
        this.idDocument = idDocument;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getStorageURL() {
        return storageURL;
    }

    public void setStorageURL(String storageURL) {
        this.storageURL = storageURL;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Document document)) return false;
        return Objects.equals(idDocument, document.idDocument)
                && Objects.equals(fileName, document.fileName)
                && Objects.equals(mimeType, document.mimeType)
                && Objects.equals(storageURL, document.storageURL)
                && Objects.equals(hash, document.hash)
                && Objects.equals(uploadedAt, document.uploadedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDocument, fileName, mimeType, storageURL, hash, uploadedAt, documentType);
    }

    @Override
    public String toString() {
        return "Document{" +
                "idDocument=" + idDocument +
                ", fileName='" + fileName + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", storageURL='" + storageURL + '\'' +
                ", hash='" + hash + '\'' +
                ", uploadedAt=" + uploadedAt +
                ", documentType=" + documentType +
                '}';
    }
}
