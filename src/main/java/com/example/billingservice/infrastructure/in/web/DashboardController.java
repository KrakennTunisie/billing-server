package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.InvoiceStatsUseCase;
import com.example.billingservice.infrastructure.out.persistance.dto.ClientInvoiceDashboardStatsMultiCurrencyDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.ClientRevenueStats;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name= "STATS Factures", description = "Statistiques des factures")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final InvoiceStatsUseCase invoiceStatsUseCase;

    @GetMapping("/clients-invoices")
    public ResponseEntity<List<ClientInvoiceDashboardStatsMultiCurrencyDTO>> getClientsInvoicesStats(
            @RequestParam(defaultValue = "2026") int year
    ) {
        return ResponseEntity.ok(
                invoiceStatsUseCase.getClientInvoicesDashboardStats(year)
        );
    }

    @GetMapping("/suppliers-invoices")
    public ResponseEntity<List<ClientInvoiceDashboardStatsMultiCurrencyDTO>> getSuppliersInvoicesStats(
            @RequestParam(defaultValue = "2026") int year
    ) {
        return ResponseEntity.ok(
                invoiceStatsUseCase.getSuppliersInvoicesDashboardStats(year)
        );
    }
    @GetMapping("/client-revenue/{idPartner}")
    public ResponseEntity<List<ClientRevenueStats>> getClientRevenue(
            @PathVariable UUID idPartner , @RequestParam String period
            ) {
        return ResponseEntity.ok(
                invoiceStatsUseCase.getClientRevenue(idPartner,period)
        );
    }
    @GetMapping("/all-client-revenue")
    public ResponseEntity<List<ClientRevenueStats>> getAllClientRevenue(
            @RequestParam String period
    ) {
        return ResponseEntity.ok(
                invoiceStatsUseCase.getAllClientRevenue(period)
        );
    }
    @GetMapping("/supplier-despenses/{idPartner}")
    public ResponseEntity<List<ClientRevenueStats>> getSupplierDespenses(
            @PathVariable UUID idPartner , @RequestParam String period
    ) {
        return ResponseEntity.ok(
                invoiceStatsUseCase.getSupplierDespenses(idPartner,period)
        );
    }
    @GetMapping("/all-supplier-despenses/{idPartner}")
    public ResponseEntity<List<ClientRevenueStats>> getAllSupplierDespenses(
            @PathVariable UUID idPartner , @RequestParam String period
    ) {
        return ResponseEntity.ok(
                invoiceStatsUseCase.getAllSupplierDespenses(idPartner,period)
        );
    }
}
