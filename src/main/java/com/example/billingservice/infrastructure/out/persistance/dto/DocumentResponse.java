package com.example.billingservice.infrastructure.out.persistance.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DocumentResponse {
    private UUID id;
    private String fileName;
    private String mimeType;
    private String storageMode;
    private String accessUrl;
    private String directUrl;
}
