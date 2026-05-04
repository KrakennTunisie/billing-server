package com.example.billingservice.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EmailQueueConfig {
    public static final String EXCHANGE = "mail.exchange";
    public static final String QUEUE = "mail.queue";
    public static final String ROUTING_KEY = "mail.send";

    public static final String DLX = "mail.dlx";
    public static final String DLQ = "mail.dlq";
    public static final String DLQ_ROUTING_KEY = "mail.failed";

    @Bean
    public DirectExchange mailExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public DirectExchange mailDeadLetterExchange() {
        return new DirectExchange(DLX);
    }

    @Bean
    public Queue mailQueue() {
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue mailDeadLetterQueue() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public Binding mailBinding() {
        return BindingBuilder.bind(mailQueue())
                .to(mailExchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding mailDlqBinding() {
        return BindingBuilder.bind(mailDeadLetterQueue())
                .to(mailDeadLetterExchange())
                .with(DLQ_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
