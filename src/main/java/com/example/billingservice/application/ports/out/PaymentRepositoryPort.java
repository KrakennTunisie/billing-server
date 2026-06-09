package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.enums.PaymentMethod;
import com.example.billingservice.domain.model.Payment;
import com.example.billingservice.infrastructure.out.persistance.dto.CreatePaymentDto;
import com.example.billingservice.infrastructure.out.persistance.dto.PaymentPageListItemDto;
import com.example.billingservice.infrastructure.out.persistance.dto.UpdatePaymentDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface PaymentRepositoryPort {
    Payment getPaymentById(UUID idPayment);
    Page<PaymentPageListItemDto> getPayments(String keyword , PaymentMethod paymentMethod, int page);
    Page<PaymentPageListItemDto> getPaymentsByPartner(UUID partnerId, String keyword, String filtre, int page);
    Page<PaymentPageListItemDto> getPaymentsByInvoice(UUID invoiceId, String keyword , PaymentMethod paymentMethod , int page);
    Payment createPayment(Payment payment);
    Payment updatePayment(Payment payment);
    void deletePayment(UUID idPayment);

    boolean existsByIdPayment(UUID idPayment);

    boolean existsByReference(String reference);
}
