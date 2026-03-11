package com.example.billingservice.infrastructure.out.persistance.entity;

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

    @Column(name = "storage_url", nullable = false, length = 500)
    private String storageURL;

    private String hash;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

}
