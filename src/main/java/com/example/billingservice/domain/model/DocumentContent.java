package com.example.billingservice.domain.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@Schema(description = "Modèle Document content")
public class DocumentContent {
    @Schema(description = "Identifiant unique", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID idDocumentContent;

    private String fileName;
    private String mimeType;

    private Long fileSize;

    private byte[] fileContent;

    @Override
    public String toString() {
        return "DocumentContent{" +
                "fileSize=" + fileSize +
                ", idDocument=" + idDocumentContent +
                '}';
    }
}
