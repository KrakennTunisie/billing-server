package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.PaymentMethod;
import com.example.billingservice.domain.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class PaymentPageListItemDto {
    UUID idPayment;

    InvoiceSummaryDTO invoice;

    BigDecimal amount;

    String currency;

    LocalDate paymentDate;

    PaymentMethod method;

    PaymentStatus paymentStatus;

    String reference;

}
