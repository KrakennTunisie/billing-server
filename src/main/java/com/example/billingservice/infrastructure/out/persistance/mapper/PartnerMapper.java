package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.Partner;
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

    public PartnerEntity toEntity (Partner partner)
    {

        // Instancier la bonne classe selon le type
        PartnerEntity entity = switch (partner.getPartnerType()) {
            case CLIENT -> new CustomerEntity();
            case SUPPLIER -> new SupplierEntity();
        };


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


    public Partner toDomain(PartnerEntity entity)
    {
        // Déduire PartnerType
        PartnerType partnerType;
        if (entity instanceof CustomerEntity) {
            partnerType = PartnerType.CLIENT;
        } else if (entity instanceof SupplierEntity) {
            partnerType = PartnerType.SUPPLIER;
        } else {
            throw new IllegalStateException("Unknown partner type: " + entity.getClass());
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
                .partnerType(partnerType)
                .rne(rne).contract(contrat).patente(patente)
                .build();

    }
}
