package com.example.billingservice.infrastructure.out.persistance.entity;

import com.example.billingservice.domain.enums.*;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "invoices")
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idInvoice;

    private String invoiceNumber;
    private Date issueDate;
    private Date dueDate;

    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @Enumerated(EnumType.STRING)
    private InvoiceComplianceStatus invoiceComplianceStatus;

    private Double totalExclTaxEUR;
    private Double totalInclTaxEUR;
    private Double totalExclTaxTND;
    private Double totalInclTaxTND;
    private Double vatRate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String creditedAccount;

    @DateTimeFormat
    private Date exchangeRateReferenceDate;

    private Double appliedExchangeRate;

    @Enumerated(EnumType.STRING)
    private ExchangeRateSource exchangeRateSource;

    private String complianceQRcode;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "invoice_document_id", referencedColumnName = "idDocument")
    private DocumentEntity invoiceDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = true)
    private PurchaseOrderEntity purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private PartnerEntity partner;

    @OneToMany(mappedBy = "invoice")
    private List<InvoiceEventEntity> invoiceEvents;

    @OneToMany(mappedBy = "invoice")
    private List<InvoiceItemEntity> invoiceItems;
}
