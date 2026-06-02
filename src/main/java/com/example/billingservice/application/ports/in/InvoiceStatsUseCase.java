package com.example.billingservice.application.ports.in;

import com.example.billingservice.infrastructure.out.persistance.dto.*;

import java.util.List;
import java.util.UUID;

public interface InvoiceStatsUseCase {

    InvoicesStatsResponse getClientsInvoicesStats(int year);

    InvoicesStatsResponse getSuppliersInvoicesStats(int year);

    ConvertedInvoiceStats getClientInvoiceStats(UUID idPartner);

    ConvertedInvoiceStats getSupplierInvoiceStats(UUID idPartner);

    List<ClientInvoiceDashboardStatsMultiCurrencyDTO> getClientInvoicesDashboardStats(int year);

    List<ClientInvoiceDashboardStatsMultiCurrencyDTO> getSuppliersInvoicesDashboardStats(int year);

    ConvertedInvoiceStats getALLClientInvoiceStats();

    ConvertedInvoiceStats getALLSupplierInvoiceStats();

    List<ClientRevenueStats> getClientRevenue(UUID partner , String periode);
    List<ClientRevenueStats> getSupplierDespenses(UUID partner , String periode);

    List<ClientRevenueStats> getAllClientRevenue( String periode);

    List<ClientRevenueStats> getAllSupplierDespenses(UUID partner , String periode);


}
