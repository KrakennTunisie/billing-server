package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.enums.InvoiceType;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.Payment;
import com.example.billingservice.infrastructure.out.persistance.dto.CreatePaymentDto;
import com.example.billingservice.infrastructure.out.persistance.dto.PaymentDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PaymentPageListItemDto;
import com.example.billingservice.infrastructure.out.persistance.dto.UpdatePaymentDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final InvoiceMapper invoiceMapper;
    private final InvoiceUseCase invoiceUseCase;
    private final DocumentMapper documentMapper;

    public PaymentEntity modelToEntity(Payment model) {
        if (model == null) {
            return null;
        }

        PaymentEntity payment = PaymentEntity.builder()
                .idPayment(model.getIdPayment())
                .invoice(invoiceMapper.toEntity(model.getInvoice()))
                .amount(model.getAmount())
                .currency(model.getCurrency())
                .paymentDate(model.getPaymentDate())
                .method(model.getMethod())
                .reference(model.getReference())
                .note(model.getNote())
                .paymentDocument(documentMapper.toEntity(model.getPaymentDocument(), DocumentType.PAYMENT))
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();

        return payment;
    }

    public Payment entityToModel(PaymentEntity entity) {
        if (entity == null) {
            return null;
        }

        Payment payment = Payment.builder()
                .idPayment(entity.getIdPayment())
                .invoice(invoiceMapper.toDomain(entity.getInvoice(), InvoiceType.SALE))
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .paymentDate(entity.getPaymentDate())
                .method(entity.getMethod())
                .reference(entity.getReference())
                .note(entity.getNote())
                .paymentDocument(documentMapper.toDomain(entity.getPaymentDocument()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

        return payment;
    }

    public Payment createDtoToModel(CreatePaymentDto dto, Document paymentDocument) {
        if (dto == null) {
            return null;
        }

        return Payment.builder()
                .invoice(invoiceUseCase.getClientInvoiceDomainById(UUID.fromString(dto.getInvoiceNumber())))
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .paymentDate(dto.getDate() != null ? dto.getDate() : LocalDate.now())
                .method(dto.getMethod())
                .reference(dto.getPaymentNumber())
                .note(dto.getNote())
                .paymentDocument(paymentDocument)
                .build();
    }

    public Payment updateDtoToModel(UpdatePaymentDTO dto, Document paymentDocument) {
        if (dto == null) {
            return null;
        }

        return Payment.builder()
                .idPayment(UUID.fromString(dto.getIdPayment()))
                .invoice(invoiceUseCase.getClientInvoiceDomainById(UUID.fromString(dto.getInvoiceNumber())))
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .paymentDate(dto.getDate() != null ? dto.getDate() : LocalDate.now())
                .method(dto.getMethod())
                .reference(dto.getPaymentNumber())
                .note(dto.getNote())
                .paymentDocument(paymentDocument)
                .build();
    }

    public PaymentPageListItemDto modelToPageListItem(Payment model) {
        if (model == null) {
            return null;
        }

        return  PaymentPageListItemDto.builder()
                .idPayment(model.getIdPayment())
                .invoice(invoiceMapper.toSummaryDTO(model.getInvoice()))
                .amount(model.getAmount())
                .currency(model.getCurrency())
                .paymentDate(model.getPaymentDate())
                .method(model.getMethod())
                .reference(model.getReference())
                .build();
    }

    public PaymentDTO modelToPaymentDTO(Payment model){
        if(model == null){
            return null;
        }

        return PaymentDTO.builder()
                .idPayment(model.getIdPayment())
                .invoice(invoiceMapper.toDetailedSummaryDTO(model.getInvoice()))
                .amount(model.getAmount())
                .currency(model.getCurrency())
                .paymentDate(model.getPaymentDate())
                .method(model.getMethod())
                .reference(model.getReference())
                .note(model.getNote())
                .paymentDocument(documentMapper.toDocumentSummary(model.getPaymentDocument()))
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }
}
