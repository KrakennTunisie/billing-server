package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.service.ReadDocumentService;
import com.example.billingservice.domain.model.DocumentContent;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.storage.type", havingValue = "database")
public class DocumentController {
    private final ReadDocumentService readDocumentService;

    @GetMapping("/{id}/content")
    public ResponseEntity<byte[]> getDocument(@PathVariable String id) {

        DocumentContent doc = readDocumentService.getDocument(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getMimeType()))
                .body(doc.getFileContent());
    }
}
