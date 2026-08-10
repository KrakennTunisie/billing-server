package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.enums.MailJobStatus;
import com.example.billingservice.domain.model.MailJob;
import com.example.billingservice.domain.model.MailJobModel;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobListItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MailJobRepositoryPort {
    MailJobModel save(MailJob mailJob);

    MailJobModel getMailById(UUID mailId);

    boolean existsById(UUID mailId);

    void updateStatus(UUID mailJobId ,MailJobStatus mailJobStatus);

    Page<MailJobListItemDTO> getMailJobByPartner(String email , String keyword , String status , int page);

    void updateMailJobStatus(UUID eventId, String status);

    void createMailJob(MailJobRequest mailJobRequest, String eventId);

}
