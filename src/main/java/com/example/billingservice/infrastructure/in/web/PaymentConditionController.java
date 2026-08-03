package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.PaymentConditionUseCase;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payment-conditions")
@RequiredArgsConstructor
public class PaymentConditionController {
    private final PaymentConditionUseCase paymentConditionUseCase;

    // ========================
    // GET ALL
    // ========================
    @GetMapping
    public ResponseEntity<List<BasePaymentConditionPageItem>> getAll() {
        return ResponseEntity.ok(paymentConditionUseCase.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<BasePaymentConditionPageItem>> getAllActive() {
        return ResponseEntity.ok(paymentConditionUseCase.getAllActive());
    }

    // ========================
    // GET BY ID
    // ========================
    @GetMapping("/{id}")
    public ResponseEntity<BasePaymentConditionDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(paymentConditionUseCase.getById(UUID.fromString(id)));
    }

    // ========================
    // GET BY CODE
    // ========================
    @GetMapping("/code/{code}")
    public ResponseEntity<BasePaymentConditionDTO> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(paymentConditionUseCase.getByCode(code));
    }

    // ========================
    // GET BY LABEL
    // ========================
    @GetMapping("/label/{label}")
    public ResponseEntity<BasePaymentConditionDTO> getByLabel(@PathVariable String label) {
        return ResponseEntity.ok(paymentConditionUseCase.getByLabel(label));
    }

    // ========================
    // CREATE
    // ========================
    @PostMapping
    public ResponseEntity<BasePaymentConditionDTO> create(
            @Valid @ModelAttribute BaseSettingCreateDTO dto) {

        return new ResponseEntity<>(
                paymentConditionUseCase.create(dto),
                HttpStatus.CREATED
        );
    }

    // ========================
    // UPDATE
    // ========================
    @PutMapping("/{id}")
    public ResponseEntity<BasePaymentConditionDTO> update(
            @PathVariable String id,
            @Valid @ModelAttribute BaseSettingUpdateDTO dto) {

        return ResponseEntity.ok(
                paymentConditionUseCase.update(dto, id)
        );
    }

    // ========================
    // ACTIVATE
    // ========================
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable String id) {
        paymentConditionUseCase.activate(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    // ========================
    // DEACTIVATE
    // ========================
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable String id) {
        paymentConditionUseCase.deactivate(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }


}
