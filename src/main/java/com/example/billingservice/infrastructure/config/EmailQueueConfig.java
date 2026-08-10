/*package com.example.billingservice.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

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
        return ExchangeBuilder
                .directExchange(EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public DirectExchange mailDeadLetterExchange() {
        return ExchangeBuilder
                .directExchange(DLX)
                .durable(true)
                .build();
    }

    @Bean
    public Queue mailQueue() {
        return QueueBuilder
                .durable(QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue mailDeadLetterQueue() {
        return QueueBuilder
                .durable(DLQ)
                .build();
    }

    @Bean
    public Binding mailBinding() {
        return BindingBuilder
                .bind(mailQueue())
                .to(mailExchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding mailDlqBinding() {
        return BindingBuilder
                .bind(mailDeadLetterQueue())
                .to(mailDeadLetterExchange())
                .with(DLQ_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);


        rabbitTemplate.setMandatory(true);

        return rabbitTemplate;
    }

    @Bean
    public RetryOperationsInterceptor mailRetryInterceptor(
            RabbitTemplate rabbitTemplate
    ) {
        MessageRecoverer recoverer = new RepublishMessageRecoverer(
                rabbitTemplate,
                DLX,
                DLQ_ROUTING_KEY
        );

        return RetryInterceptorBuilder
                .stateless()
                .maxAttempts(3)
                .backOffOptions(
                        1000,   // initial interval: 1s
                        2.0,    // multiplier
                        10000   // max interval: 10s
                )
                .recoverer(recoverer)
                .build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter,
            RetryOperationsInterceptor mailRetryInterceptor
    ) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setAdviceChain(mailRetryInterceptor);


        factory.setDefaultRequeueRejected(false);

        return factory;
    }
}
**/