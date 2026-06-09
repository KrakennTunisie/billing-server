package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.InvoicePaymentSnchronizeUseCase;
import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@AllArgsConstructor
public class InvoicePaymentSynchronizeService implements InvoicePaymentSnchronizeUseCase {
    private final InvoiceUseCase invoiceUseCase;

    @Override
    public boolean validatePaymentAmount(UUID invoiceId, Double amount) {
        InvoiceDTO invoiceDTO =  invoiceUseCase.getClientInvoiceById(invoiceId);

        double remainingAmount = invoiceDTO.getRemainingAmount() - amount;

        return remainingAmount >= 0  || remainingAmount <= getTotal(invoiceDTO);
    }

    @Override
    public void validatePayment(UUID invoiceId, Double amount) {
        InvoiceDTO invoiceDTO =  invoiceUseCase.getClientInvoiceById(invoiceId);

        invoiceUseCase.updateClientInvoiceRemainingAmount(invoiceId, amount);

        if(Objects.equals(invoiceDTO.getRemainingAmount(), amount)){
            invoiceUseCase.updateClientInvoiceStatus(invoiceId, InvoiceStatus.PAID);
        }
        else {
            invoiceUseCase.updateClientInvoiceStatus(invoiceId, InvoiceStatus.PARTIALLY_PAID);
        }
    }

    private double getTotal(InvoiceDTO invoiceDTO){
       return switch (invoiceDTO.getInvoiceCurrency()){
            case USD -> invoiceDTO.getTotalInclTaxUSD();
            case TND -> invoiceDTO.getTotalInclTaxTND();
            case EUR -> invoiceDTO.getTotalInclTaxEUR();
        };
    }
}
