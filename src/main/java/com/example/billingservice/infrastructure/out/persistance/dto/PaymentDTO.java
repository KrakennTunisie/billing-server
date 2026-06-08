package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.PaymentMethod;
import com.example.billingservice.domain.model.Invoice;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class PaymentDTO {
    private UUID idPayment;

    private InvoiceDetailedSummaryDTO invoice;

    private BigDecimal amount;

    private String currency;

    private LocalDate paymentDate;

    private PaymentMethod method;

    private String reference;

    private String note;

    private DocumentSummaryDTO paymentDocument;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
