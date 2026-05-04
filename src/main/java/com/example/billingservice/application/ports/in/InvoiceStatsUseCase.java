package com.example.billingservice.application.ports.in;

import com.example.billingservice.infrastructure.out.persistance.dto.*;

import java.util.List;
import java.util.UUID;

public interface InvoiceStatsUseCase {

    InvoicesStatsResponse getClientsInvoicesStats(int year);

    InvoicesStatsResponse getSuppliersInvoicesStats(int year);

    ConvertedInvoiceStats getClientInvoiceStats(UUID idPartner);

    PartnerInvoiceStatsResponse getSupplierInvoiceStats(UUID idPartner);

    List<ClientInvoiceDashboardStatsMultiCurrencyDTO> getClientInvoicesDashboardStats(int year);

    List<ClientInvoiceDashboardStatsMultiCurrencyDTO> getSuppliersInvoicesDashboardStats(int year);

    ConvertedInvoiceStats getALLClientInvoiceStats();


}
