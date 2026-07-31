package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.PaymentUseCase;
import com.example.billingservice.application.service.GenerateInvoiceNumberService;
import com.example.billingservice.domain.enums.PaymentStatus;
import com.example.billingservice.domain.enums.SequenceNumberType;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@Tag(name= "Payment API", description = "Gestion des paiements")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentUseCase paymentUseCase;
    private final GenerateInvoiceNumberService generateInvoiceNumberService;

    @Operation(summary = "Numéro facture suivant", description = "Générer le numéro de paiement suivant")
    @GetMapping(path = "/next-number")
    public ResponseEntity <NextNumberDTO> generateNextPaymentNumber()
    {
        NextNumberDTO nextNumberDTO = NextNumberDTO.builder()
                .value(generateInvoiceNumberService.generate(SequenceNumberType.PAYMENT))
                .build();
        return ResponseEntity.ok(nextNumberDTO);
    }

    @Operation(summary = "Liste des paiements")
    @GetMapping("")
    public ResponseEntity<Page<PaymentPageListItemDto>> getAllPayments(@RequestParam(required = false) String keyword,
                                                                        @RequestParam(required = false) String filter,
                                                                        @RequestParam int page )
    {
        return ResponseEntity.ok(paymentUseCase.getPayments(keyword, filter, page));
    }

    @Operation(summary = "Créer un paiement", description = "Ajoute un nouveau paiement")
    @PostMapping(path = "/")
    public ResponseEntity <PaymentDTO> createPayment (@Valid @ModelAttribute CreatePaymentDto form) throws IOException {
        return ResponseEntity.status(201).body(paymentUseCase.createPayment(form));
    }

    @GetMapping("/invoice/{id}")
    @Operation(summary = "Récupérer les paiements d'une facture")
    public ResponseEntity<Page<PaymentPageListItemDto>> getAllPaymentsByIdInvoice(
            @Parameter(description = "ID du paiement")@PathVariable String id,
            @RequestParam(required = false) String keyword,
           @RequestParam(required = false) String filter,
           @RequestParam int page )
    {
        return ResponseEntity.ok(paymentUseCase.getPaymentsByInvoice(UUID.fromString(id), keyword, filter, page));
    }

    @GetMapping("/partner/{id}")
    @Operation(summary = "Récupérer les paiements d'une facture")
    public ResponseEntity<Page<PaymentPageListItemDto>> getAllPaymentsByIdPayment(
            @Parameter(description = "ID du partenaire")@PathVariable String id,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String filter,
            @RequestParam int page )
    {
        return ResponseEntity.ok(paymentUseCase.getPaymentsByPartner(UUID.fromString(id), keyword, filter, page));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un paiement")
    public ResponseEntity<PaymentDTO> getPaymentById(@Parameter(description = "ID du paiement")@PathVariable String id)
    {
        return ResponseEntity.ok(paymentUseCase.getPaymentById(UUID.fromString(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Suppression d'un paiement")
    public ResponseEntity<Void> deletePayment(@Parameter(description = "ID du paiement") @PathVariable String id)
    {
        paymentUseCase.deletePayment(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Modification d'un paiement")
    public ResponseEntity<PaymentDTO> UpdatePayment(
            @Parameter(description = "ID du paiement") @PathVariable String id,
            @Valid @ModelAttribute UpdatePaymentDTO updatePaymentDTO) throws IOException {
        return ResponseEntity.ok(paymentUseCase.updatePayment(UUID.fromString(id), updatePaymentDTO));
    }
    @PatchMapping("/updateStatus/{id}")
    @Operation(summary = "Modification la status d'un reçu de paiement")
    public ResponseEntity<Void> UpdatePaymentStatus(
            @Parameter(description = "ID du paiement") @PathVariable String id,
            @RequestParam PaymentStatus paymentStatus)  {
        paymentUseCase.updatePaymentStatus(UUID.fromString(id),paymentStatus);
        return ResponseEntity.noContent().build();
    }
}
