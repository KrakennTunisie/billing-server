package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.MailJobUseCase;
import com.example.billingservice.application.ports.out.MailJobRepositoryPort;
import com.example.billingservice.application.ports.out.MailNotificationRepositoryPort;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.MailJobModel;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobListItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobRequest;
import com.example.billingservice.infrastructure.out.persistance.entity.MailNotificationJobEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MailJobService implements MailJobUseCase {

    private final MailJobRepositoryPort mailJobRepositoryPort;
    private final MailNotificationRepositoryPort mailNotificationRepositoryPort;

    @Override
    public Page<MailJobListItemDTO> getEmailByPartner(String email, String keyword, String status, int page) {
        return mailJobRepositoryPort.getMailJobByPartner(email, keyword, status, page);
    }

    @Override
    public MailJobModel getMailById(UUID mailId) {
        if(!mailJobRepositoryPort.existsById(mailId)){
            throw BillingException.notFound("Mail", String.valueOf(mailId));
        }
        return mailJobRepositoryPort.getMailById(mailId);
    }

    @Override
    public void updateMailJobStatus(UUID eventId, String status) {
        mailJobRepositoryPort.updateMailJobStatus(eventId, status);
    }

    @Override
    @Transactional
    public void createMailJob(MailJobRequest mailJobRequest) {

        MailNotificationJobEntity mailNotificationJob = mailNotificationRepositoryPort.createMailJob(mailJobRequest);

        mailJobRepositoryPort.createMailJob(mailJobRequest, String.valueOf(mailNotificationJob.getEventId()));
    }
}
