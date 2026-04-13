package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.InvoiceType;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoicePageItemDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ClientInvoicesRepositoryPort {
    Page<InvoicePageItemDTO> findAllInvoices(String keyword , InvoiceStatus status , int page, InvoiceType type);


    InvoiceDTO save(Invoice invoice);

    InvoiceDTO update(Invoice invoice);

    InvoiceDTO updateStatus(UUID invoiceId, InvoiceStatus newStatus);

    InvoiceDTO getById(UUID idInvoice);

    Invoice getInvoice(UUID idInvoice);

    void delete(UUID idInvoice);

    boolean existsByInvoiceNumber(String invoiceNumber);

    boolean existsByInvoiceId(UUID invoiceId);

}
