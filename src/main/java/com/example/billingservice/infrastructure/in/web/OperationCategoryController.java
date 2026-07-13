package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.OperationCategoryUseCase;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseItemOperationCategoryDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseItemOperationCategoryPageItem;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingUpdateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/operation-categories")
@RequiredArgsConstructor
public class OperationCategoryController {

        private final OperationCategoryUseCase operationCategoryService;

        // ========================
        // GET ALL
        // ========================
        @GetMapping
        public ResponseEntity<List<BaseItemOperationCategoryPageItem>> getAll() {
            return ResponseEntity.ok(operationCategoryService.getAll());
        }

        // ========================
        // GET ALL
        // ========================
        @GetMapping("/active")
        public ResponseEntity<List<BaseItemOperationCategoryPageItem>> getAllActive() {
            return ResponseEntity.ok(operationCategoryService.getAllActive());
        }

        // ========================
        // GET BY ID
        // ========================
        @GetMapping("/{id}")
        public ResponseEntity<BaseItemOperationCategoryDTO> getById(@PathVariable String id) {
            return ResponseEntity.ok(operationCategoryService.getById(UUID.fromString(id)));
        }

        // ========================
        // GET BY CODE
        // ========================
        @GetMapping("/code/{code}")
        public ResponseEntity<BaseItemOperationCategoryDTO> getByCode(@PathVariable String code) {
            return ResponseEntity.ok(operationCategoryService.getByCode(code));
        }

        // ========================
        // GET BY LABEL
        // ========================
        @GetMapping("/label/{label}")
        public ResponseEntity<BaseItemOperationCategoryDTO> getByLabel(@PathVariable String label) {
            return ResponseEntity.ok(operationCategoryService.getByLabel(label));
        }

        // ========================
        // CREATE
        // ========================
        @PostMapping
        public ResponseEntity<BaseItemOperationCategoryDTO> create(
                @Valid @ModelAttribute BaseSettingCreateDTO dto) {

            return new ResponseEntity<>(
                    operationCategoryService.create(dto),
                    HttpStatus.CREATED
            );
        }

        // ========================
        // UPDATE
        // ========================
        @PutMapping("/{id}")
        public ResponseEntity<BaseItemOperationCategoryDTO> update(
                @PathVariable String id,
                @Valid @ModelAttribute BaseSettingUpdateDTO dto) {

            return ResponseEntity.ok(
                    operationCategoryService.update(dto, id)
            );
        }

        // ========================
        // ACTIVATE
        // ========================
        @PatchMapping("/{id}/activate")
        public ResponseEntity<Void> activate(@PathVariable String id) {
            operationCategoryService.activate(UUID.fromString(id));
            return ResponseEntity.noContent().build();
        }

        // ========================
        // DEACTIVATE
        // ========================
        @PatchMapping("/{id}/deactivate")
        public ResponseEntity<Void> deactivate(@PathVariable String id) {
            operationCategoryService.deactivate(UUID.fromString(id));
            return ResponseEntity.noContent().build();
        }


}
