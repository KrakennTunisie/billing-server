package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.PaymentMethod;
import com.example.billingservice.domain.enums.PaymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CreatePaymentDto {
    @NotNull(message = "La référence du paiement est obligatoire")
    @Setter
    String paymentNumber;

    @NotNull(message = "La référence de la facture est obligatoire")
    String invoiceNumber;

    @NotNull(message = "Le montant du paiement est obligatoire")
    @DecimalMin(value = "0.001", message = "Le montant doit être supérieur à 0")
    BigDecimal amount;

    @NotBlank(message = "La devise est obligatoire")
    @Size(min = 3, max = 3, message = "La devise doit contenir exactement 3 caractères")
    String currency;

    LocalDate date;

    @NotNull(message = "Le mode de paiement est obligatoire")
    PaymentMethod method;

    @NotNull(message = "La status de reçu de paiement")
    PaymentStatus paymentStatus;

    String comment;

    @NotNull(message = "Le document de paiement est obligatoire")
    private MultipartFile paymentDocument;

}
