package com.example.billingservice.infrastructure.out.queue;

import com.example.billingservice.application.ports.out.EmailJobPublisherPort;
import com.example.billingservice.domain.model.MailJob;
import com.example.billingservice.infrastructure.config.EmailQueueConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitMqEmailJobPublisherAdapter implements EmailJobPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(MailJob job) {
        log.info("Publishing mail job. to={}, subject={}", job.to(), job.subject());

        rabbitTemplate.convertAndSend(
                EmailQueueConfig.EXCHANGE,
                EmailQueueConfig.ROUTING_KEY,
                job
        );

        log.info("Mail job published. to={}", job.to());
    }
}
