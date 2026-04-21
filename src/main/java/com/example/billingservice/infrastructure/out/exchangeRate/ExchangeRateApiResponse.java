package com.example.billingservice.infrastructure.out.exchangeRate;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ExchangeRateApiResponse {

    private boolean success;
    private Query query;
    private Info info;
    private BigDecimal result;

    @Getter
    public static class Query {
        private String from;
        private String to;
        private BigDecimal amount;

        @Override
        public String toString() {
            return "Query{" +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    ", amount=" + amount +
                    '}';
        }
    }

    @Getter
    public static class Info {
        private long timestamp;
        private BigDecimal quote;

        @Override
        public String toString() {
            return "Info{" +
                    "timestamp=" + timestamp +
                    ", quote=" + quote +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ExchangeRateApiResponse{" +
                "success=" + success +
                ", query=" + query +
                ", info=" + info +
                ", result=" + result +
                '}';
    }
}
