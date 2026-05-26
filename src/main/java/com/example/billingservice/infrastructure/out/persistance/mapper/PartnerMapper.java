package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.application.service.UploadDocumentService;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.model.AuditLog;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.infrastructure.out.persistance.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


@Component
@RequiredArgsConstructor
public class PartnerMapper {
    private final DocumentMapper documentMapper;
    private final AddressMapper addressMapper;
    private final UploadDocumentService uploadDocumentService ;

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
       // entity.setIdPartner(partner.getIdPartner());
        entity.setActive(partner.isActive());
        entity.setEnablePortal(partner.isEnablePortal());
        entity.setPartnerName(partner.getPartnerName());
        entity.setMaritalStatus(partner.getMaritalStatus());
        entity.setCompanyName(partner.getCompanyName());
        entity.setDisplayName(partner.getDisplayName());
        entity.setEmail(partner.getEmail());
        entity.setPersonnelPhoneNumber(partner.getPersonnelPhoneNumber());
        entity.setProfessionnalPhoneNumber(partner.getProfessionnalPhoneNumber());
        entity.setBillingAddressEntity(addressMapper.toEntity(partner.getBillingAddress()));
        entity.setShippingAddressEntity(addressMapper.toEntity(partner.getShippingAddress()));
        entity.setLanguage(partner.getLanguage());
        entity.setCurrency(partner.getCurrency());
        entity.setPaymentCondition(partner.getPaymentCondition());
        entity.setTaxRate(partner.getTaxRate());
        entity.setTaxRegistrationNumber(partner.getTaxRegistrationNumber());
        entity.setIban(partner.getIban());
        entity.setRne(rne);
        entity.setContract(contract);
        entity.setPatente(patente);
       /* System.out.println(entity.getBillingAddressEntity().getAddressType() + entity.getBillingAddressEntity().getStreet());
        System.out.println(entity.getShippingAddressEntity().getAddressType() + entity.getBillingAddressEntity().getStreet());*/
        return  entity;
    }

    public PartnerEntity updateEntity(UpdatePartnerDTO partner, PartnerEntity existingEntity) {


        existingEntity.setActive(partner.isActive());
        existingEntity.setEnablePortal(partner.isEnablePortal());
        existingEntity.setPartnerName(partner.getPartnerName());
        existingEntity.setMaritalStatus(partner.getMaritalStatus());
        existingEntity.setCompanyName(partner.getCompanyName());
        existingEntity.setDisplayName(partner.getDisplayName());
        existingEntity.setEmail(partner.getEmail());
        existingEntity.setPersonnelPhoneNumber(partner.getPersonnelPhoneNumber());
        existingEntity.setProfessionnalPhoneNumber(partner.getProfessionnalPhoneNumber());
        existingEntity.setLanguage(partner.getLanguage());
        existingEntity.setCurrency(partner.getCurrency());
        existingEntity.setPaymentCondition(partner.getPaymentCondition());
        existingEntity.setTaxRate(partner.getTaxRate());
        existingEntity.setTaxRegistrationNumber(partner.getTaxRegistrationNumber());
        existingEntity.setIban(partner.getIban());
        existingEntity.setBillingAddressEntity(addressMapper.updateToEntity(existingEntity.getBillingAddressEntity(),partner.getBillingAddress()));
        existingEntity.setShippingAddressEntity(addressMapper.updateToEntity(existingEntity.getShippingAddressEntity(),partner.getShippingAddress()));


        return existingEntity;
    }




    public Partner toDomain(PartnerEntity entity, PartnerType type)
    {

        //RNE
        Document rne =documentMapper.toDomain(entity.getRne());
        //Patente
        Document patente = documentMapper.toDomain(entity.getPatente());
        //Contrat
        Document contrat = documentMapper.toDomain(entity.getContract());

        return Partner.builder()
                .idPartner(entity.getIdPartner())
                .active(entity.isActive())
                .enablePortal(entity.isEnablePortal())
                .partnerName(entity.getPartnerName())
                .partnerType(type)
                .maritalStatus(entity.getMaritalStatus())
                .companyName(entity.getCompanyName())
                .displayName(entity.getDisplayName())
                .email(entity.getEmail())
                .personnelPhoneNumber(entity.getPersonnelPhoneNumber())
                .professionnalPhoneNumber(entity.getProfessionnalPhoneNumber())
                .billingAddress(addressMapper.toDomain(entity.getBillingAddressEntity()))
                .shippingAddress(addressMapper.toDomain(entity.getShippingAddressEntity()))
                .Language(entity.getLanguage())
                .currency(entity.getCurrency())
                .taxRegistrationNumber(entity.getTaxRegistrationNumber())
                .paymentCondition(entity.getPaymentCondition())
                .taxRate(entity.getTaxRate())
                .iban(entity.getIban())
                .partnerType(type)
                .rne(rne).contract(contrat).patente(patente)
                .build();

    }

    public PartnerItemDTO toItemDTO(Partner partner) {

        return PartnerItemDTO.builder()
                .idPartner(partner.getIdPartner())
                .partnerName(partner.getPartnerName())
                .companyName(partner.getCompanyName())
                .email(partner.getEmail())
                .professionnalPhoneNumber(partner.getProfessionnalPhoneNumber())
                .taxRegistrationNumber(partner.getTaxRegistrationNumber())
                .partnerType(partner.getPartnerType())
                .billingAddress(partner.getBillingAddress())
                .iban(partner.getIban())
                .build();
    }


    public static void updatePartnerFromDTO(UpdatePartnerDTO dto, Partner partner) {
        if (dto == null || partner == null) {
            return;
        }

        partner.setActive(dto.isActive());
        partner.setEnablePortal(dto.isEnablePortal());

        if (dto.getPartnerName() != null) {
            partner.setPartnerName(dto.getPartnerName());
        }
        if (dto.getMaritalStatus() != null) {
            partner.setMaritalStatus(dto.getMaritalStatus());
        }
        if (dto.getPartnerType() != null) {
            partner.setPartnerType(dto.getPartnerType());
        }
        if (dto.getCompanyName() != null) {
            partner.setCompanyName(dto.getCompanyName());
        }
        if (dto.getDisplayName()!= null) {
            partner.setDisplayName(dto.getDisplayName());
        }
        if (dto.getPersonnelPhoneNumber()!= null) {
            partner.setPersonnelPhoneNumber(dto.getPersonnelPhoneNumber());
        }
        if (dto.getProfessionnalPhoneNumber()!= null) {
            partner.setProfessionnalPhoneNumber(dto.getProfessionnalPhoneNumber());
        }
        if (dto.getBillingAddress()!= null) {
            partner.setBillingAddress(dto.getBillingAddress());
        }
        if (dto.getShippingAddress()!= null) {
            partner.setShippingAddress(dto.getShippingAddress());
        }
        if (dto.getLanguage()!= null) {
            partner.setLanguage(dto.getLanguage());
        }
        if (dto.getCurrency()!= null) {
            partner.setCurrency(dto.getCurrency());
        }
        if (dto.getTaxRate()!= null) {
            partner.setTaxRate(dto.getTaxRate());
        }
        if (dto.getTaxRegistrationNumber()!= null) {
            partner.setTaxRegistrationNumber(dto.getTaxRegistrationNumber());
        }
        if (dto.getPaymentCondition()!= null) {
            partner.setPaymentCondition(dto.getPaymentCondition());
        }
        if (dto.getEmail() != null) {
            partner.setEmail(dto.getEmail());
        }
        if (dto.getIban() != null) {
            partner.setIban(dto.getIban());
        }

    }

    public  Partner createPartnerFromDTO(PartnerForm dto) throws IOException {
        if (dto == null ) {
            return null;
        }

        UploadedFile rne = new UploadedFile(
                dto.getRne().getOriginalFilename(),
                dto.getRne().getContentType(),
                dto.getRne().getInputStream(),
                dto.getRne().getSize()
        );

        UploadedFile contract = new UploadedFile(
                dto.getContract().getOriginalFilename(),
                dto.getContract().getContentType(),
                dto.getContract().getInputStream(),
                dto.getContract().getSize()
        );

        UploadedFile patente = new UploadedFile(
                dto.getPatente().getOriginalFilename(),
                dto.getPatente().getContentType(),
                dto.getPatente().getInputStream(),
                dto.getPatente().getSize()
        );

        Document uploadedRne = uploadDocumentService.upload(dto.getTaxRegistrationNumber(), DocumentType.RNE, rne);
        Document uploadedContract = uploadDocumentService.upload(dto.getTaxRegistrationNumber(), DocumentType.CONTRACT, contract);
        Document uploadedPatente = uploadDocumentService.upload(dto.getTaxRegistrationNumber(), DocumentType.PATENT, patente);
        System.out.println(dto.getEnablePortal());
        return Partner.builder()
                .active(Boolean.parseBoolean(dto.getActive()))
                .enablePortal(Boolean.parseBoolean(dto.getEnablePortal()))
                .partnerName(dto.getPartnerName())
                .partnerType(dto.getPartnerType())
                .maritalStatus(dto.getMaritalStatus())
                .companyName(dto.getCompanyName())
                .displayName(dto.getDisplayName())
                .email(dto.getEmail())
                .personnelPhoneNumber(dto.getPersonnelPhoneNumber())
                .professionnalPhoneNumber(dto.getProfessionnalPhoneNumber())
                .billingAddress(addressMapper.DTOtoDomain(dto.getBillingAddress()))
                .shippingAddress(addressMapper.DTOtoDomain(dto.getShippingAddress()))
                .Language(dto.getLanguage())
                .currency(dto.getCurrency())
                .taxRegistrationNumber(dto.getTaxRegistrationNumber())
                .paymentCondition(dto.getPaymentCondition())
                .taxRate(dto.getTaxRate())
                .iban(dto.getIban())
                .rne(uploadedRne).contract(uploadedContract).patente(uploadedPatente)
                .build();
    }

    public PartnerSummaryDTO toSummaryDTO(Partner partner) {
        if (partner == null) {
            return null;
        }

        return PartnerSummaryDTO.builder()
                .idPartner(partner.getIdPartner())
                .name(partner.getCompanyName())
                .email(partner.getEmail())
                .address(partner.getBillingAddress())
                .phoneNumber(partner.getProfessionnalPhoneNumber())
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
