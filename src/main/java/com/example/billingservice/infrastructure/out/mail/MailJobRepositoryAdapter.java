package com.example.billingservice.infrastructure.out.mail;

import com.example.billingservice.application.ports.out.MailJobRepositoryPort;
import com.example.billingservice.application.ports.out.MailNotificationRepositoryPort;
import com.example.billingservice.domain.enums.MailJobStatus;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.MailJob;
import com.example.billingservice.domain.model.MailJobModel;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobListItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobRequest;
import com.example.billingservice.infrastructure.out.persistance.entity.MailJobEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.MailNotificationJobEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.MailJobMapper;
import com.example.billingservice.infrastructure.out.persistance.mapper.MailJobRequestMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.MailJobRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MailJobRepositoryAdapter implements MailJobRepositoryPort {
    private final MailJobRepository mailJobRepository;
    private final MailJobMapper mailJobMapper;
    private final MailJobRequestMapper mailJobRequestMapper;

    private final MailNotificationRepositoryPort mailNotificationRepositoryPort;

    @Override
    @Transactional
    public MailJobModel save(MailJob mailJob) {
        MailJobModel mailJobModel = mailJobMapper.mailJobToModel(mailJob);
        MailJobEntity entity = mailJobRepository.save(mailJobMapper.toEntity(mailJobModel));
        return mailJobMapper.entityToModel(entity);
    }

    @Override
    public MailJobModel getMailById(UUID mailId) {
        MailJobEntity mailJobEntity = mailJobRepository.getReferenceById(mailId);
        return mailJobMapper.entityToModel(mailJobEntity);
    }

    @Override
    public boolean existsById(UUID mailId) {
        return mailJobRepository.existsMailJobEntityById(mailId);
    }

    @Override
    @Transactional
    public void updateStatus(UUID mailJobId, MailJobStatus mailJobStatus) {
        MailJobEntity entity = mailJobRepository.getReferenceById(mailJobId);
        entity.setStatus(mailJobStatus);
        mailJobRepository.save(entity);
    }

    @Override
    public Page<MailJobListItemDTO> getMailJobByPartner(String email, String keyword, String status, int page) {
        try {

            PageRequest pageRequest = PageRequest.of(page, 5, Sort.by("date").descending());
            Page<MailJobEntity> entities = mailJobRepository.getMailJobByPartner(email, pageRequest);

            List<MailJobListItemDTO> mails = entities.getContent()
                    .stream()
                    .map(mailJobMapper::entityToModel)
                    .map(mailJobMapper::toMailJobListItemDTO)
                    .collect(Collectors.toList());

            return new PageImpl<>(mails, pageRequest, entities.getTotalElements());

        } catch (DataAccessException ex) {
            throw BillingException.internalError("Erreur de fetch des factures: " + ex.getMessage());
        }
    }

    @Override
    public void createMailJob(MailJobRequest mailJobRequest, String eventId) {
        MailJobEntity mailJobEntity = mailJobRequestMapper.toEntity(mailJobRequest, eventId);
        mailJobRepository.save(mailJobEntity);
    }

    @Override
    @Transactional
    public void updateMailJobStatus(UUID eventId, String status) {
        if(!mailNotificationRepositoryPort.existsByEventId(eventId)){
            throw BillingException.notFound("Mail notification", String.valueOf(eventId));
        }

        mailNotificationRepositoryPort.updateStatus(eventId, status);

        this.updateStatus(eventId, status);
    }



    private void updateStatus(UUID eventId, String status){
        if(!mailJobRepository.existsByEventId(String.valueOf(eventId))){
            throw BillingException.notFound("Mail job", String.valueOf(eventId));
        }

        MailJobEntity mailJob = mailJobRepository.findByEventId(String.valueOf(eventId));

        mailJob.setStatus(MailJobStatus.valueOf(status));
    }
}
