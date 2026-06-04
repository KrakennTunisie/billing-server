package com.example.billingservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class ExchangeRateClientConfig {

    @Bean
    public WebClient exchangeRateWebClient(
            @Value("${spring.exchange-rate.api.base-url}") String baseUrl
    ) {
        HttpClient httpClient = HttpClient.create()
                .followRedirect(true);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/json")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
