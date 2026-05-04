package com.example.billingservice.infrastructure.out.persistance.dto;

import java.io.InputStream;

public record UploadedFile(String originalFileName,
                           String mimeType,
                           InputStream content,
                           long size
) {
}
