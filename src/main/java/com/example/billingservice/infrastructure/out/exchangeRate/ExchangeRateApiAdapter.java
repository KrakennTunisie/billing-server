package com.example.billingservice.infrastructure.out.exchangeRate;

import com.example.billingservice.application.ports.out.ExchangeRatePort;
import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.model.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class ExchangeRateApiAdapter implements ExchangeRatePort {

    private final WebClient exchangeRateWebClient;

    @Value("${spring.exchange-rate.api.key:}")
    private String apiKey;

    @Override
    public ExchangeRate fetchExchangeRate(String fromCurrency, String toCurrency, LocalDate date) {
        ExchangeRateApiResponse response = exchangeRateWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/convert")
                        .queryParam("access_key", apiKey)
                        .queryParam("from", fromCurrency)
                        .queryParam("to", toCurrency)
                        .queryParam("amount", 1)
                        .build())
                .retrieve()
                .bodyToMono(ExchangeRateApiResponse.class)
                .block();

        System.out.println("response : "+response);

        if (response == null) {
            throw new IllegalStateException("Exchange rate API returned null response");
        }

        // ✅ Check success
        if (!response.isSuccess()) {
            throw new IllegalStateException("Exchange rate API returned success=false");
        }

        if (response.getInfo() == null || response.getInfo().getQuote() == null) {
            throw new IllegalStateException("Missing exchange rate info in API response");
        }

        BigDecimal quote = response.getInfo().getQuote();

        // ✅ Convert UNIX timestamp → LocalDateTime
        LocalDateTime fetchedAt = Instant.ofEpochSecond(response.getInfo().getTimestamp())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return ExchangeRate.builder()
                .fromCurrency(InvoiceCurrency.valueOf(fromCurrency))
                .toCurrency(InvoiceCurrency.valueOf(toCurrency))
                .quote(quote)
                .rateDate(date)
                .fetchedAt(fetchedAt)
                .source("currencylayer")
                .build();
    }

}
