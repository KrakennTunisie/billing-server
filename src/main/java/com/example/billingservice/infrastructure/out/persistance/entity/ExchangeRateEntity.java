package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.InvoiceCurrency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "exchange_rates",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_exchange_rate_pair_date",
                        columnNames = {"from_currency", "to_currency", "rate_date"}
                )
        }
)
@Getter
@Setter
public class ExchangeRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idExchangeRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_currency", nullable = false, length = 3)
    private InvoiceCurrency fromCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_currency", nullable = false, length = 3)
    private InvoiceCurrency toCurrency;

    @Column(name = "quote", nullable = false, precision = 19, scale = 6)
    private BigDecimal quote;

    @Column(name = "rate_date", nullable = false)
    private LocalDate rateDate;

    @Column(name = "fetched_at", nullable = false)
    private LocalDateTime fetchedAt;

    @Column(name = "source", length = 100)
    private String source;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
