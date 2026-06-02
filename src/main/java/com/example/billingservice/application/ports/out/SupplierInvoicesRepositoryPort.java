package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.InvoiceType;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface SupplierInvoicesRepositoryPort {
    Page<InvoicePageItemDTO> findAllInvoices(String keyword , InvoiceStatus status , int page, InvoiceType type);

    List<InvoicePageItemDTO> getSupplierTopInvoices(UUID idSupplier);

    InvoiceDTO save(Invoice invoice);

    InvoiceDTO update(Invoice invoice);

    InvoiceDTO updateStatus(UUID invoiceId, InvoiceStatus newStatus);

    InvoiceDTO getById(UUID idInvoice);

    InvoiceDTO getInvoiceByInvoiceNumber(String invoiceNumber);

    Invoice getInvoice(UUID idInvoice);

    InvoicesStatsResponse getSuppliersInvoicesStats(int year);

    ConvertedInvoiceStats getSupplierInvoicesStats(UUID idPartner);

    List<ClientInvoiceDashboardStatsMultiCurrencyDTO> getSupplierInvoicesDashboardStats(int year);

    ConvertedInvoiceStats getAllSupplierInvoiceCountStats(InvoiceStatus pendingStatus);

    void delete(UUID idInvoice);

    boolean existsByInvoiceNumber(String invoiceNumber);

    boolean existsByInvoiceId(UUID invoiceId);

    List <Invoice> getSupplierInvoices(UUID idpartner);

    List<ClientRevenueStats> getSupplierDespensesByPeriod(UUID idPartner , String period);

    List<ClientRevenueStats> getAllSupplierDespensesByPeriod(UUID idPartner , String period);


}
