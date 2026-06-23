package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.MailJobUseCase;
import com.example.billingservice.application.ports.out.MailJobRepositoryPort;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.MailJobModel;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobListItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MailJobService implements MailJobUseCase {

    private final MailJobRepositoryPort mailJobRepositoryPort;

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
}
