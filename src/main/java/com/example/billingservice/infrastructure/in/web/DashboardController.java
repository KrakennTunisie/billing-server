package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.InvoiceStatsUseCase;
import com.example.billingservice.infrastructure.out.persistance.dto.ClientInvoiceDashboardStatsMultiCurrencyDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name= "STATS Factures", description = "Statistiques des factures")
@RestController
@RequestMapping("/api/dashboard")
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
}
