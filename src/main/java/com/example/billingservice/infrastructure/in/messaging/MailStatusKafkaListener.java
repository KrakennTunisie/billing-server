package com.example.billingservice.infrastructure.in.messaging;

import com.example.billingservice.application.ports.in.MailJobUseCase;
import com.example.billingservice.infrastructure.out.persistance.dto.MailResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailStatusKafkaListener {
    private final MailJobUseCase mailJobUseCase;

    @KafkaListener(topics = "mail.status.events", groupId = "billing-service")
    public void onMailStatus(MailResultDTO dto) {
        mailJobUseCase.updateMailJobStatus(dto.eventId(), dto.status());
    }
}
