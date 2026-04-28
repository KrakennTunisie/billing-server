package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.CurrencyConversionUseCase;
import com.example.billingservice.domain.model.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/exchange-rate")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final CurrencyConversionUseCase currencyConversionUseCase;

    @GetMapping("/content")
    public ResponseEntity<ExchangeRate> getExchangeRate(@RequestParam String fromCurrency,
                                                        @RequestParam String toCurrency) {



        ExchangeRate exchangeRate = currencyConversionUseCase
                .convert( fromCurrency, toCurrency, LocalDate.now());

        return ResponseEntity.ok()
                .body(exchangeRate);
    }
}
