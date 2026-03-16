package com.example.billingservice.infrastructure.out.storage;

import com.example.billingservice.infrastructure.out.persistance.dto.StoredDocument;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import com.example.billingservice.application.ports.out.DocumentStoragePort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.exceptions.DocumentStorageException;
import com.example.billingservice.shared.DocumentUtils;
import com.example.billingservice.shared.HashUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;


@Component
public class LocalDocumentStorageAdapter implements DocumentStoragePort {

    private final Path rootPath;
    private final String publicBaseUrl;

    public LocalDocumentStorageAdapter(
            @Value("${spring.storage.local.root-path}") String rootPath,
            @Value("${spring.storage.local.public-base-url}") String publicBaseUrl
    ) {
        this.rootPath = Paths.get(rootPath);
        this.publicBaseUrl = publicBaseUrl;
    }

    @Override
    public StoredDocument store(UUID ownerId, UploadedFile file, DocumentType documentType) {
        DocumentUtils.validateFile(file);

        try {
            String ownerFolder = DocumentUtils.resolveOwnerFolder(documentType);

            Path folder = rootPath
                    .resolve(ownerFolder)
                    .resolve(ownerId.toString())
                    .resolve(documentType.name().toLowerCase());

            Files.createDirectories(folder);

            String extension = DocumentUtils.extractExtension(file.originalFileName());
            String generatedName = UUID.randomUUID() + extension;

            Path target = folder.resolve(generatedName);

            Files.write(
                    target,
                    file.content(),
                    StandardOpenOption.CREATE_NEW
            );

            String url = publicBaseUrl + "/uploads/"
                    + ownerFolder + "/"
                    + ownerId + "/"
                    + documentType.name().toLowerCase() + "/"
                    + generatedName;

            return new StoredDocument(
                    file.originalFileName(),
                    file.mimeType(),
                    url,
                    HashUtils.sha256(file.content())
            );

        } catch (IOException e) {
            throw new DocumentStorageException("Failed to store file locally",e);
        }
    }



}