package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.model.MailAttachment;
import com.example.billingservice.domain.model.MailAttachmentMetadata;
import com.example.billingservice.infrastructure.out.persistance.entity.MailAttachmentMetadataEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailAttachmentMetadataMapper {

    public  MailAttachmentMetadata entityToModel(MailAttachmentMetadataEntity entity) {
        if (entity == null) return null;
        return MailAttachmentMetadata.builder()
                .id(entity.getId())
                .fileName(entity.getFileName())
                .filePath(entity.getFilePath())
                .build();
    }

    public  MailAttachmentMetadata toModel(MailAttachment mailAttachment) {
        if (mailAttachment == null) return null;
        return MailAttachmentMetadata.builder()
                .fileName(mailAttachment.filename())
                .filePath(mailAttachment.filePath())
                .build();
    }

    public  MailAttachmentMetadataEntity toEntity(MailAttachmentMetadata model) {
        if (model == null) return null;

        MailAttachmentMetadataEntity mailAttachmentMetadataEntity = new MailAttachmentMetadataEntity();
        mailAttachmentMetadataEntity.setId(model.getId());
        mailAttachmentMetadataEntity.setFileName(model.getFileName());
        mailAttachmentMetadataEntity.setFilePath(model.getFilePath());
                // mailJob is set in MailJobMapper to avoid circular reference
        return mailAttachmentMetadataEntity;
    }


}
