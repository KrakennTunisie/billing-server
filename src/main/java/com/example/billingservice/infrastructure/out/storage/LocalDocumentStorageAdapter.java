package com.example.billingservice.infrastructure.out.storage;

import com.example.billingservice.domain.enums.DocumentStorageMode;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import com.example.billingservice.application.ports.out.DocumentStoragePort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.exceptions.DocumentStorageException;
import com.example.billingservice.infrastructure.out.persistance.entity.DocumentEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.DocumentMapper;
import com.example.billingservice.shared.DocumentUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;


@Component
@ConditionalOnProperty(name = "spring.storage.type", havingValue = "local")
public class LocalDocumentStorageAdapter implements DocumentStoragePort {

    private final Path rootPath;
    private final String publicBaseUrl;
    private final DocumentMapper documentMapper;

    public LocalDocumentStorageAdapter(
            @Value("${spring.storage.local.root-path}") String rootPath,
            @Value("${spring.storage.local.public-base-url}") String publicBaseUrl,
            DocumentMapper documentMapper)
    {
        this.documentMapper = documentMapper;
        this.rootPath = Paths.get(rootPath);
        this.publicBaseUrl = publicBaseUrl;
    }

    @Override
    public Document store(String ownerReference, UploadedFile file, DocumentType documentType) {
        DocumentUtils.validateFile(file);

        try {
            Document document = documentMapper.fromUploadedFile(file, documentType);
            document.setStorageMode(DocumentStorageMode.FILESYSTEM);

            DocumentEntity documentEntity = documentMapper.toEntity(document, documentType);

            String ownerFolder = DocumentUtils.resolveOwnerFolder(documentType);

            Path folder = rootPath
                    .resolve(ownerFolder)
                    .resolve(ownerReference)
                    .resolve(documentType.name().toLowerCase());

            Files.createDirectories(folder);

            String extension = DocumentUtils.extractExtension(file.originalFileName());
            String generatedName = ownerReference+ documentType.name() + extension;

            Path target = folder.resolve(generatedName);

            Files.write(
                    target,
                    file.content(),
                    StandardOpenOption.CREATE_NEW
            );

            String url = publicBaseUrl + "/storage/"
                    + ownerFolder + "/"
                    + ownerReference + "/"
                    + documentType.name().toLowerCase() + "/"
                    + generatedName;
            documentEntity.setStorageURL(url);

            return documentMapper.toDomain(documentEntity);

        } catch (FileAlreadyExistsException e) {
            throw BillingException.alreadyExists(
                    "Document",
                    "fichier",
                    file.originalFileName()
            );
        } catch (IOException e) {
                throw new DocumentStorageException("Erreur de sauvegarde du document",e);
            }
        }



}