package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.InvoiceType;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoicePageItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoicesStatsResponse;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerInvoiceStatsResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface SupplierInvoicesRepositoryPort {
    Page<InvoicePageItemDTO> findAllInvoices(String keyword , InvoiceStatus status , int page, InvoiceType type);


    InvoiceDTO save(Invoice invoice);

    InvoiceDTO update(Invoice invoice);

    InvoiceDTO updateStatus(UUID invoiceId, InvoiceStatus newStatus);

    InvoiceDTO getById(UUID idInvoice);

    Invoice getInvoice(UUID idInvoice);

    InvoicesStatsResponse getSuppliersInvoicesStats(int year);

    PartnerInvoiceStatsResponse getSupplierInvoicesStats(UUID idPartner);


    void delete(UUID idInvoice);

    boolean existsByInvoiceNumber(String invoiceNumber);

    boolean existsByInvoiceId(UUID invoiceId);

}
