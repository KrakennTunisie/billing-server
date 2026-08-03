package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.enums.PaymentStatus;
import com.example.billingservice.domain.model.Payment;
import com.example.billingservice.infrastructure.out.persistance.dto.CreatePaymentDto;
import com.example.billingservice.infrastructure.out.persistance.dto.PaymentDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PaymentPageListItemDto;
import com.example.billingservice.infrastructure.out.persistance.dto.UpdatePaymentDTO;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.UUID;

public interface PaymentUseCase {
    PaymentDTO getPaymentById(UUID idPayment);
    Page<PaymentPageListItemDto> getPaymentsByInvoice(UUID invoiceId, String keyword , String filtre , int page);
    Page<PaymentPageListItemDto> getPaymentsByPartner(UUID partnerId, String keyword , String filtre , int page);
    Page<PaymentPageListItemDto> getPayments(String keyword , String filtre , int page);
    PaymentDTO createPayment(CreatePaymentDto createPaymentDto) throws IOException;
    PaymentDTO updatePayment(UUID idPayment, UpdatePaymentDTO updatePaymentDTO) throws IOException;
    void updatePaymentStatus(UUID idPayment, PaymentStatus paymentStatus);
    void deletePayment(UUID idPayment);

    boolean existsByIdPayment(UUID idPayment);

    boolean existsByReference(String reference);
}
