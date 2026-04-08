package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerSummaryDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.UpdatePartnerDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.CustomerEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.DocumentEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.PartnerEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.SupplierEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PartnerMapper {
    private final DocumentMapper documentMapper;

    public  PartnerEntity toEntity(Partner partner)
    {

        // Instancier la bonne classe selon le type
        PartnerEntity entity = createEntityByPartnerType(partner.getPartnerType());

        //RNE
        DocumentEntity rne = documentMapper.toRneEntity(partner.getRne());

        //Patente
        DocumentEntity patente = documentMapper.toPatenteEntity(partner.getPatente());

        //Contract
        DocumentEntity contract = documentMapper.toContractEntity(partner.getContract());

        //Partner
        entity.setIdPartner(partner.getIdPartner());
        entity.setName(partner.getName());
        entity.setEmail(partner.getEmail());
        entity.setPhoneNumber(partner.getPhoneNumber());
        entity.setTaxRegistrationNumber(partner.getTaxRegistrationNumber());
        entity.setCountry(partner.getCountry());
        entity.setAddress((partner.getAddress()));
        entity.setIban(partner.getIban());
        entity.setRne(rne);
        entity.setContract(contract);
        entity.setPatente(patente);
        return  entity;
    }


    public Partner toDomain(PartnerEntity entity, PartnerType type)
    {
        // Déduire PartnerType
        if (type!=PartnerType.CLIENT && type!=PartnerType.SUPPLIER){
            throw new IllegalStateException("Unknown partner type: " + type);
        }
        //RNE
        Document rne =documentMapper.toDomain(entity.getRne());
        //Patente
        Document patente = documentMapper.toDomain(entity.getPatente());
        //Contrat
        Document contrat = documentMapper.toDomain(entity.getContract());
        return Partner.builder()
                .idPartner(entity.getIdPartner())
                .name(entity.getName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .taxRegistrationNumber(entity.getTaxRegistrationNumber())
                .country(entity.getCountry())
                .address(entity.getAddress())
                .iban(entity.getIban())
                .partnerType(type)
                .rne(rne).contract(contrat).patente(patente)
                .build();

    }

    public PartnerItemDTO toItemDTO(PartnerEntity entity) {
        PartnerType partnerType;
        if (entity instanceof CustomerEntity) {
            partnerType = PartnerType.CLIENT;
        } else if (entity instanceof SupplierEntity) {
            partnerType = PartnerType.SUPPLIER;
        } else {
            throw new IllegalStateException("Unknown partner type: " + entity.getClass());
        }
        return PartnerItemDTO.builder()
                .idPartner(entity.getIdPartner())
                .name(entity.getName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .taxRegistrationNumber(entity.getTaxRegistrationNumber())
                .partnerType(partnerType)
                .country(entity.getCountry())
                .address(entity.getAddress())
                .iban(entity.getIban())
                .build();
    }


    public static void updatePartnerFromDTO(UpdatePartnerDTO dto, Partner partner) {
        if (dto == null || partner == null) {
            return;
        }

        if (dto.getName() != null) {
            partner.setName(dto.getName());
        }

        if (dto.getEmail() != null) {
            partner.setEmail(dto.getEmail());
        }

        if (dto.getPhoneNumber() != null) {
            partner.setPhoneNumber(dto.getPhoneNumber());
        }

        if (dto.getTaxRegistrationNumber() != null) {
            partner.setTaxRegistrationNumber(dto.getTaxRegistrationNumber());
        }

        if (dto.getCountry() != null) {
            partner.setCountry(dto.getCountry());
        }

        if (dto.getAddress() != null) {
            partner.setAddress(dto.getAddress());
        }

        if (dto.getIban() != null) {
            partner.setIban(dto.getIban());
        }

        if (dto.getPartnerType() != null) {
            partner.setPartnerType(dto.getPartnerType());
        }
    }

    public PartnerSummaryDTO toSummaryDTO(Partner partner) {
        if (partner == null) {
            return null;
        }

        return PartnerSummaryDTO.builder()
                .idPartner(partner.getIdPartner())
                .name(partner.getName())
                .email(partner.getEmail())
                .address(partner.getAddress())// optionnel
                .phoneNumber(partner.getPhoneNumber())      // optionnel
                .partnerType(partner.getPartnerType())
                .build();
    }

    private PartnerEntity createEntityByPartnerType(PartnerType partnerType) {
        if (partnerType == null) {
            throw new IllegalArgumentException("PartnerType must not be null");
        }

        return switch (partnerType) {
            case CLIENT -> new CustomerEntity();
            case SUPPLIER -> new SupplierEntity();
        };
    }
}
