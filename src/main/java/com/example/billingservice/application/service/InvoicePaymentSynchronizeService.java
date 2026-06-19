package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.InvoicePaymentSnchronizeUseCase;
import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Component
@AllArgsConstructor
public class InvoicePaymentSynchronizeService implements InvoicePaymentSnchronizeUseCase {
    private final InvoiceUseCase invoiceUseCase;

    @Transactional
    @Override
    public void applyPayment(UUID invoiceId, BigDecimal amount) {

        InvoiceDTO invoice = invoiceUseCase.getClientInvoiceById(invoiceId);

        BigDecimal total = BigDecimal.valueOf(getTotal(invoice));

        BigDecimal oldRemaining = BigDecimal.valueOf(invoice.getRemainingAmount());

        BigDecimal paidAmount = total.subtract(oldRemaining);

        BigDecimal remaining = total.subtract(paidAmount.add(amount));

        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            throw BillingException.badRequest("Payment exceeds invoice amount");
        }
        invoiceUseCase.updateClientInvoiceRemainingAmount(
                invoiceId,
                remaining.doubleValue()
        );

        invoiceUseCase.updateClientInvoiceStatus(
                invoiceId,
                determineStatus(total, remaining)
        );
    }

    private InvoiceStatus determineStatus(
            BigDecimal total,
            BigDecimal remaining){

        if(remaining.compareTo(BigDecimal.ZERO)==0)
            return InvoiceStatus.PAID;

        if(remaining.compareTo(total)==0)
            return InvoiceStatus.TO_COLLECT;

        return InvoiceStatus.PARTIALLY_PAID;
    }

    private double getTotal(InvoiceDTO invoiceDTO){
        return switch (invoiceDTO.getInvoiceCurrency()){
            case USD -> invoiceDTO.getTotalInclTaxUSD();
            case TND -> invoiceDTO.getTotalInclTaxTND();
            case EUR -> invoiceDTO.getTotalInclTaxEUR();
        };
    }
}
