package com.example.billingservice.domain.model;

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
public class Payment {

    private UUID idPayment;

    private Invoice invoice;

    private BigDecimal amount;

    private String currency;

    private PaymentStatus paymentStatus;

    private LocalDate paymentDate;

    private PaymentMethod method;

    private String reference;

    private String note;

    private Document paymentDocument;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
