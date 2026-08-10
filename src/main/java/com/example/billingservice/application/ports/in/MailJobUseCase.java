package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.MailJobModel;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobListItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;


public interface MailJobUseCase {

    Page<MailJobListItemDTO> getEmailByPartner(String email , String keyword , String status , int page);

    MailJobModel getMailById(UUID mailId);

    void updateMailJobStatus(UUID eventId, String status);

    void createMailJob(MailJobRequest mailJobRequest);

}
