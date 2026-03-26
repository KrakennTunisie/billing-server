package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.DocumentStorageMode;
import com.example.billingservice.domain.enums.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@Schema(description = "Modèle Document")
public class Document {
    @Schema(description = "Identifiant unique", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID idDocument;
    @Schema(description = "Nom document", example ="Contrat client v22")
    private String fileName;
    @Schema(description = "Type MIME du fichier", example = "application/pdf")
    private String mimeType;
    @Schema(description = "URL de stockage du fichier", example = "https://example.com/storage/contrat.pdf")
    private String storageURL;
    @Schema(description = "Empreinte du fichier (SHA-256)", example = "a1b2c3d4e5f6...")
    private String hash;
    @Schema(description = "Mode de stockage du document", example = "FILESYSTEM")
    private DocumentStorageMode storageMode;
    @Schema(description = "Date et heure d'upload", example = "2026-03-09T09:55:22")
    private LocalDateTime uploadedAt;

    private DocumentType documentType;

    @Schema(description = "Contenu Document")
    private DocumentContent content;

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
                ", storageMode=" + storageMode +
                ", uploadedAt=" + uploadedAt +
                ", documentType=" + documentType +
                ", content=" + content +
                '}';
    }
}
