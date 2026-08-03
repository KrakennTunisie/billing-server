package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.MailJobStatus;
import com.example.billingservice.domain.model.MailJob;
import com.example.billingservice.domain.model.MailJobModel;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobListItemDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.MailAttachmentMetadataEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.MailJobEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MailJobMapper {

    private final MailAttachmentMetadataMapper mailAttachmentMetadataMapper;

    public MailJobModel entityToModel(MailJobEntity entity) {
        if (entity == null) return null;
        return MailJobModel.builder()
                .idMailJob(entity.getId())
                .to(entity.getToEmail())
                .subject(entity.getSubject())
                .body(entity.getBody())
                .date(entity.getDate())
                .status(entity.getStatus())
                .attachments(entity.getAttachments().stream()
                        .map(mailAttachmentMetadataMapper::entityToModel)
                        .collect(Collectors.toList()))
                .build();
    }

    public MailJobModel mailJobToModel(MailJob mailJob) {
        if (mailJob == null) return null;
        // Si vous avez un Date
        Date date = new Date();

// Conversion en LocalDateTime
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return MailJobModel.builder()
                //.idMailJob(mailJob.uuid())
                .to(mailJob.to())
                .subject(mailJob.subject())
                .body(mailJob.body())
                .date(localDateTime)
                .status(MailJobStatus.CREATED)
                .attachments(mailJob.attachments().stream()
                        .map(mailAttachmentMetadataMapper::toModel)
                        .collect(Collectors.toList()))
                .build();
    }

    public MailJobEntity toEntity(MailJobModel model) {
        if (model == null) return null;

        MailJobEntity mailJob = new MailJobEntity();
        mailJob.setId(model.getIdMailJob());
        mailJob.setToEmail(model.getTo());
        mailJob.setDate(model.getDate());
        mailJob.setSubject(model.getSubject());
        mailJob.setBody(model.getBody());
        mailJob.setStatus(model.getStatus());

        // Map attachments and set mailJob reference
        if (model.getAttachments() != null) {
            model.getAttachments().forEach(att -> {
                MailAttachmentMetadataEntity entity = mailAttachmentMetadataMapper.toEntity(att);
                entity.setMailJob(mailJob);
                mailJob.getAttachments().add(entity);
            });
        }

        return mailJob;
    }

    public MailJobListItemDTO toMailJobListItemDTO (MailJobModel mailJobModel){
        if (mailJobModel == null) return null;
       return  MailJobListItemDTO.builder()
                .idMailJob(mailJobModel.getIdMailJob())
                .to(mailJobModel.getTo())
                .subject(mailJobModel.getSubject())
                .status(mailJobModel.getStatus())
                .date(mailJobModel.getDate())
               .attachments(mailJobModel.getAttachments())
                .build();

    }


}
