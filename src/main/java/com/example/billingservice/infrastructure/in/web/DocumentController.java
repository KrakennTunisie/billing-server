package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.GetDocumentUseCase;
import com.example.billingservice.application.service.ReadDocumentService;
import com.example.billingservice.domain.model.DocumentContent;
import com.example.billingservice.infrastructure.out.persistance.dto.DocumentReadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final GetDocumentUseCase getDocumentUseCase;

    @GetMapping("/{id}/content")
    public ResponseEntity<byte[]> getDocument(@PathVariable String id) {

        DocumentContent doc = getDocumentUseCase.getDocument(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getMimeType()))
                .body(doc.getFileContent());
    }

    @GetMapping("/{id}/file-content")
    public ResponseEntity<byte[]> getDocumentContent(@PathVariable String id) {

        DocumentReadFile doc = getDocumentUseCase.getFileAttachment(UUID.fromString(id));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.mimeType()))
                .body(doc.content());
    }

}
