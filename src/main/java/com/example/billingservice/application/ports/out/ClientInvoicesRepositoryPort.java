package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.InvoiceType;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ClientInvoicesRepositoryPort {
    Page<InvoicePageItemDTO> findAllInvoices(String keyword , InvoiceStatus status , int page, InvoiceType type);

    InvoiceDTO save(Invoice invoice);

    InvoiceDTO update(Invoice invoice);

    InvoiceDTO updateStatus(UUID invoiceId, InvoiceStatus newStatus);

    InvoiceDTO getById(UUID idInvoice);

    Invoice getInvoice(UUID idInvoice);

    InvoicesStatsResponse getClientsInvoicesStats(int year);

    ConvertedInvoiceStats getClientInvoiceStats(UUID idPartner);

    List<ClientInvoiceDashboardStatsMultiCurrencyDTO> getClientInvoicesDashboardStats(int year);

    ConvertedInvoiceStats getAllClientInvoiceCountStats(InvoiceStatus pendingStatus);



    void delete(UUID idInvoice);

    boolean existsByInvoiceNumber(String invoiceNumber);

    boolean existsByInvoiceId(UUID invoiceId);

}
