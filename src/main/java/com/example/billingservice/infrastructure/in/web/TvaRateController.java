package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.TVARateUseCase;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tva-rates")
@RequiredArgsConstructor
public class TvaRateController {
    private final TVARateUseCase tvaRateUseCase;

    // ========================
    // GET ALL
    // ========================
    @GetMapping
    public ResponseEntity<List<BaseTVARatePageItem>> getAll() {
        return ResponseEntity.ok(tvaRateUseCase.getAll());
    }


    @GetMapping("/active")
    public ResponseEntity<List<BaseTVARatePageItem>> getAllActive() {
        return ResponseEntity.ok(tvaRateUseCase.getAllActive());
    }

    // ========================
    // GET BY ID
    // ========================
    @GetMapping("/{id}")
    public ResponseEntity<BaseTVARateDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(tvaRateUseCase.getById(UUID.fromString(id)));
    }

    // ========================
    // GET BY CODE
    // ========================
    @GetMapping("/code/{code}")
    public ResponseEntity<BaseTVARateDTO> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(tvaRateUseCase.getByCode(code));
    }

    // ========================
    // GET BY LABEL
    // ========================
    @GetMapping("/label/{label}")
    public ResponseEntity<BaseTVARateDTO> getByLabel(@PathVariable String label) {
        return ResponseEntity.ok(tvaRateUseCase.getByLabel(label));
    }

    // ========================
    // CREATE
    // ========================
    @PostMapping
    public ResponseEntity<BaseTVARateDTO> create(
            @Valid @ModelAttribute BaseSettingCreateDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(tvaRateUseCase.create(dto));
    }

    // ========================
    // UPDATE
    // ========================
    @PutMapping("/{id}")
    public ResponseEntity<BaseTVARateDTO> update(
            @PathVariable String id,
            @Valid @ModelAttribute BaseSettingUpdateDTO dto) {

        return ResponseEntity.ok(
                tvaRateUseCase.update(dto, id)
        );
    }

    // ========================
    // ACTIVATE
    // ========================
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable String id) {
        tvaRateUseCase.activate(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    // ========================
    // DEACTIVATE
    // ========================
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable String id) {
        tvaRateUseCase.deactivate(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }


}
