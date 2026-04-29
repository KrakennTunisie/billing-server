package com.example.billingservice.infrastructure.out.storage;

import com.example.billingservice.application.ports.out.DocumentReaderPort;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.DocumentContent;
import com.example.billingservice.infrastructure.out.persistance.dto.DocumentReadFile;
import com.example.billingservice.infrastructure.out.persistance.entity.DocumentContentEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.DocumentEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.DocumentContentMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.DocumentContentRepository;
import com.example.billingservice.infrastructure.out.persistance.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@AllArgsConstructor
public class DocumentReaderAdapter implements DocumentReaderPort {
    private final DocumentContentRepository documentContentRepository;
    private final DocumentContentMapper documentContentMapper;
    private final DocumentRepository documentRepository;


    @Override
    public DocumentContent read(String idDocument) {

        DocumentContentEntity document = documentContentRepository.findById(UUID.fromString(idDocument))
                .orElseThrow(() -> new BillingException(HttpStatus.NOT_FOUND, "404","Not found"));

        return documentContentMapper.toDomain(document);

    }

    @Override
    public DocumentReadFile getFileAttachment(UUID idDocument) {
            DocumentEntity document = documentRepository.findById(idDocument)
                    .orElseThrow(()-> BillingException.notFound("Document", String.valueOf(idDocument)));

            byte[] content = switch (document.getStorageMode()) {
                case DATABASE -> readFromDatabase(document);
                case FILESYSTEM -> readFromFileSystem(document.getStorageURL());
                case CLOUD_URL -> null;
            };

            return new DocumentReadFile(
                    document.getIdDocument(),
                    document.getFileName(),
                    document.getMimeType(),
                    content
            );
    }

    private byte[] readFromDatabase(DocumentEntity document) {
        if (document.getContent() == null) {
            throw BillingException.notFound("document: " , String.valueOf(document.getIdDocument()));
        }
        DocumentContentEntity documentContentEntity = documentContentRepository.getReferenceById(document.getContent().getIdDocument());


        return documentContentEntity.getFileContent();
    }

    private byte[] readFromFileSystem(String storageURL) {
        try {
            URI uri = URI.create(storageURL);

            String path = uri.getPath();
            // /api/storage/invoices/FAC-2026-00024/invoice/FAC-2026-00024INVOICE.pdf

            String relativePath = path.replaceFirst("^/api/storage/", "");

            Path filePath = Paths.get("storage").resolve(relativePath).normalize();

            return Files.readAllBytes(filePath);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read document from filesystem: " + storageURL, e);
        }
    }

    /*    private byte[] readFromCloud(String storageURL) {
            return cloudDocumentStorageClient.download(storageURL);
        }*/
}
