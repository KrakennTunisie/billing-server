package com.example.billingservice.shared;

import com.example.billingservice.infrastructure.out.persistance.dto.ClientInvoiceDashboardStatsMultiCurrencyDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.ConvertedInvoiceStats;
import com.example.billingservice.infrastructure.out.persistance.projections.ClientInvoiceDashboardStatsProjection;
import com.example.billingservice.infrastructure.out.persistance.projections.PartnerInvoiceCountStatsProjection;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class StatsHelper {
    public static ConvertedInvoiceStats getStats(BigDecimal totalAmountTND, BigDecimal pendingAmountTND,
                                                 BigDecimal totalAmountEUR, BigDecimal pendingAmountEUR,
                                                 BigDecimal totalAmountUSD, BigDecimal pendingAmountUSD,
                                                 PartnerInvoiceCountStatsProjection countStats ){
        Long totalInvoices = defaultLong(countStats.getTotalInvoices());
        Long paidInvoices = defaultLong(countStats.getPaidInvoices());
        Long pendingInvoices = defaultLong(countStats.getPendingInvoices());

        BigDecimal averageInvoice = totalInvoices > 0
                ? totalAmountTND.divide(BigDecimal.valueOf(totalInvoices), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        return new  ConvertedInvoiceStats(
                scale(totalAmountTND),
                scale(totalAmountEUR),
                scale(totalAmountUSD),
                defaultLong(totalInvoices),
                defaultLong(paidInvoices),
                defaultLong(pendingInvoices),
                scale(pendingAmountTND),
                scale(pendingAmountEUR),
                scale(pendingAmountUSD),
                scale(averageInvoice),
                scale(averageInvoice),
                scale(averageInvoice)
        );
    }

    public static ClientInvoiceDashboardStatsMultiCurrencyDTO getDetailedStats(ClientInvoiceDashboardStatsProjection row,
                                                                               BigDecimal totalAmountTND,
                                                                               BigDecimal totalAmountEUR,
                                                                               BigDecimal totalAmountUSD){
        return  new ClientInvoiceDashboardStatsMultiCurrencyDTO(
                row.getId(),
                row.getClient(),
                scale(totalAmountTND),
                scale(totalAmountEUR),
                scale(totalAmountUSD),
                row.getMonth()
        );

    }

    private static BigDecimal scale(BigDecimal value) {
        return value == null
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : value.setScale(2, RoundingMode.HALF_UP);
    }

    private static Long defaultLong(Long value) {
        return value == null ? 0L : value;
    }
}
