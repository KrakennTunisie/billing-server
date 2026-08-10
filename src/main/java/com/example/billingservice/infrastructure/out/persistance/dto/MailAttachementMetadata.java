package com.example.billingservice.infrastructure.out.persistance.dto;

import java.util.UUID;

public record MailAttachementMetadata(
         UUID id,

         String idDocument,

         String fileName,

         String filePath,

         String contentType
) {
}
