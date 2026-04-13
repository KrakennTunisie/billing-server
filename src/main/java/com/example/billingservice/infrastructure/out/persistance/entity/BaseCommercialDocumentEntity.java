package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.ExchangeRateSource;
import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseCommercialDocumentEntity {

    private String reference;
    private Date issueDate;


    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    @Enumerated(EnumType.STRING)
    private InvoiceCurrency currency;


    private Double vatRate;

    @DateTimeFormat
    private Date exchangeRateReferenceDate;

    private Double appliedExchangeRate;

    @Enumerated(EnumType.STRING)
    private ExchangeRateSource exchangeRateSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private PartnerEntity partner;


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;


}
