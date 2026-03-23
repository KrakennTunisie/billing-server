package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.DocumentStorageMode;
import com.example.billingservice.domain.enums.DocumentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Setter
@Getter
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idDocument;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private String fileName;

    @Column(name = "mime_type", nullable = false, length = 120)
    private String mimeType;

    private String hash;

    @Enumerated(EnumType.STRING)
    private DocumentStorageMode storageMode;

    private String storageURL;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;


    @Lob
    private byte[] fileContent;

}
