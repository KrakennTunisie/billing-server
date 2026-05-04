package com.example.billingservice.infrastructure.in.scheduler;

import com.example.billingservice.application.ports.in.CurrencyConversionUseCase;
import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.model.ExchangeRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateScheduler {
    private final CurrencyConversionUseCase currencyConversionUseCase;

    private static final List<String> TARGET_CURRENCIES = List.of(InvoiceCurrency.EUR.name(),InvoiceCurrency.USD.name());
    private static final String BASE_CURRENCY = InvoiceCurrency.TND.name();

    /**
     * Every day at midnight Tunisia time
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Africa/Tunis")
    public void fetchDailyExchangeRates() {
        LocalDate today = LocalDate.now(ZoneId.of("Africa/Tunis"));

        log.info("Starting daily exchange rate fetch for date {}", today);

        for (String targetCurrency : TARGET_CURRENCIES) {
            try {
               ExchangeRate exchangeRate =  currencyConversionUseCase.convert(
                        BASE_CURRENCY,
                        targetCurrency,
                        today
                );

                log.info("Rate fetched successfully: {} -> {}: {}",
                        exchangeRate.getFromCurrency(), exchangeRate.getToCurrency(), exchangeRate.getQuote() );

                // Anti too many requests
                Thread.sleep(1500);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Exchange rate scheduler interrupted", e);
                return;

            } catch (Exception e) {
                log.error(
                        "Failed to fetch exchange rate {} -> {} for date {}",
                        BASE_CURRENCY,
                        targetCurrency,
                        today,
                        e
                );
            }
        }

        log.info("Daily exchange rate fetch finished");
    }
}
