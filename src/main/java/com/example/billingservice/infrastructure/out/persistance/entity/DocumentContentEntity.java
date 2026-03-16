package com.example.billingservice.infrastructure.out.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "document_content")
@Getter
@Setter
public class DocumentContentEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String mimeType;

    @Lob
    @Column(nullable = false)
    private byte[] fileData;


}