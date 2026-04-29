package com.example.billingservice.infrastructure.out.queue;

import com.example.billingservice.application.ports.out.EmailSenderPort;
import com.example.billingservice.domain.model.MailJob;
import com.example.billingservice.infrastructure.config.EmailQueueConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class EmailJobConsumer {
    private final EmailSenderPort emailSenderPort;

    @RabbitListener(queues = EmailQueueConfig.QUEUE)
    public void consume(MailJob job) {

        log.info("Consuming mail job. to={}, subject={}", job.to(), job.subject());

        try {
            emailSenderPort.sendEmail(job);
            log.info("Mail sent successfully. to={}, subject={}", job.to(), job.subject());
        } catch (Exception e) {
            log.error("Mail sending failed. to={}, subject={}, error={}",
                    job.to(), job.subject(), e.getMessage(), e);

            throw e;
        }
    }
}
