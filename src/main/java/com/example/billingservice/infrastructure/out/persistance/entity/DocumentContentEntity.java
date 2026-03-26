package com.example.billingservice.infrastructure.out.persistance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "document_contents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentContentEntity {

    @Id
    @Column(name = "id_document", nullable = false)
    private UUID idDocument;

    private String fileName;
    private String mimeType;

    @Column(name = "file_content", nullable = false)
    private byte[] fileContent;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;
}
