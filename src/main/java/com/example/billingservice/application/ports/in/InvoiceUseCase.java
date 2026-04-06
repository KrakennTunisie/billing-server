package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoicePageItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

public interface InvoiceUseCase {
    Page<InvoicePageItemDTO> getClientsInvoices(String keyword , String status , int page);

    InvoiceDTO createInvoice(InvoiceCreateDTO invoice) throws IOException;

    @Transactional
    InvoiceDTO updateInvoice(InvoiceUpdateDTO invoiceUpdateDTO) throws IOException;

    InvoiceDTO updateInvoiceStatus(UUID invoiceId, InvoiceStatus invoiceStatus);

    InvoiceDTO getInvoiceById(UUID invoiceId);

    void deleteInvoice(UUID invoiceId);

    boolean existsByInvoiceNumber(String invoiceNumber);

    boolean existsByInvoiceId(UUID invoiceId);


}
