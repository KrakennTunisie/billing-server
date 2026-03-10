package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.entity.DocumentEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.PartnerEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PartnerMapper {

    public PartnerEntity toEntity (Partner partner)
    {
        PartnerEntity entity = new PartnerEntity();

        //RNE
        DocumentEntity rne = new DocumentEntity();
        rne.setFileName(partner.getRne().getFileName());
        rne.setStorageURL(partner.getRne().getStorageURL());
        rne.setMimeType(partner.getRne().getMimeType());
        rne.setHash(partner.getRne().getHash());
        rne.setUploadedAt(LocalDateTime.now());
        rne.setDocumentType(DocumentType.RNE);

        //Patente
        DocumentEntity patente = new DocumentEntity();
        patente.setFileName(partner.getPatente().getFileName());
        patente.setStorageURL(partner.getPatente().getStorageURL());
        patente.setMimeType(partner.getPatente().getMimeType());
        patente.setHash(partner.getPatente().getHash());
        patente.setUploadedAt(LocalDateTime.now());
        patente.setDocumentType(DocumentType.PATENT);

        //Contract

        DocumentEntity contract = new DocumentEntity();
        contract.setFileName(partner.getContract().getFileName());
        contract.setStorageURL(partner.getContract().getStorageURL());
        contract.setMimeType(partner.getContract().getMimeType());
        contract.setHash(partner.getContract().getHash());
        contract.setUploadedAt(LocalDateTime.now());
        contract.setDocumentType(DocumentType.CONTRACT);

        //Partner
        entity.setIdPartner(partner.getIdPartner());
        entity.setName(partner.getName());
        entity.setEmail(partner.getEmail());
        entity.setPhoneNumber(partner.getPhoneNumber());
        entity.setTaxRegistrationNumber(partner.getTaxRegistrationNumber());
        entity.setCountry(partner.getCountry());
        entity.setAddress((partner.getAddress()));
        entity.setIban(partner.getIban());
        entity.setPartnerType(partner.getPartnerType());
        entity.setRne(rne);
        entity.setContract(contract);
        entity.setPatente(patente);
        return  entity;
    }
    public Partner toDomain(PartnerEntity entity)
    {
        //RNE

        Document rne =Document.builder().idDocument(entity.getRne().getIdDocument()).fileName(entity.getRne().getFileName())
            .mimeType(entity.getRne().getMimeType()).storageURL(entity.getRne().getStorageURL()).hash(entity.getRne().getStorageURL())
                .uploadedAt(entity.getRne().getUploadedAt()).documentType(entity.getRne().getDocumentType()).build();

        //Patente
        Document patente =Document.builder().idDocument(entity.getPatente().getIdDocument()).fileName(entity.getPatente().getFileName())
                .mimeType(entity.getPatente().getMimeType()).storageURL(entity.getPatente().getStorageURL()).hash(entity.getPatente().getStorageURL())
                .uploadedAt(entity.getPatente().getUploadedAt()).documentType(entity.getPatente().getDocumentType()).build();

        //Contrat
        Document contrat =Document.builder().idDocument(entity.getContract().getIdDocument()).fileName(entity.getContract().getFileName())
                .mimeType(entity.getContract().getMimeType()).storageURL(entity.getContract().getStorageURL()).hash(entity.getContract().getStorageURL())
                .uploadedAt(entity.getContract().getUploadedAt()).documentType(entity.getContract().getDocumentType()).build();

        return Partner.builder()
                .idPartner(entity.getIdPartner())
                .name(entity.getName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .taxRegistrationNumber(entity.getTaxRegistrationNumber())
                .country(entity.getCountry())
                .address(entity.getAddress())
                .iban(entity.getIban())
                .partnerType(entity.getPartnerType())
                .rne(rne).contract(contrat).patente(patente)
                .build();

    }
}
