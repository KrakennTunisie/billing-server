package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.PartnerDTO;
import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.application.ports.out.PartnerRepositoryPort;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.model.Partner;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerService implements PartnerUseCase {

    private  final PartnerRepositoryPort partnerRepositoryPort;

    @Override
    public Partner createPartner(PartnerDTO partner) {

        Partner partnerEntity = Partner.builder().name(partner.getName()).email(partner.getEmail()).phoneNumber(partner.getPhoneNumber())
                .taxRegistrationNumber(partner.getTaxRegistrationNumber()).country(partner.getCountry())
                .address(partner.getAddress()).iban(partner.getIban()).partnerType(partner.getPartnerType())
                 .rne(partner.getRne()).contract(partner.getContrat())
                .patente(partner.getPatente()).build();
        return partnerRepositoryPort.save(partnerEntity);
    }

    @Override
    public Optional<Partner> getById(String id) {
        return partnerRepositoryPort.findById(id);
    }

    @Override
    public List<Partner> getAll() {
        return  partnerRepositoryPort.findAll();
    }

    @Override
    public void deletePartner(String id) {
      partnerRepositoryPort.deleteById(id);
    }

    @Override
    public Partner getByName(String name) {
        return partnerRepositoryPort.findByName(name);
    }

    @Override
    public Partner getByEmail(String email) {
        return partnerRepositoryPort.findByEmail(email);
    }

    @Override
    public Partner getByTaxRegistrationNumber(String TaxRegistrationNumber) {
        return partnerRepositoryPort.findByTaxRegistrationNumber(TaxRegistrationNumber);
    }

    @Override
    public List <Partner> getByPartnerType(PartnerType partnerType) {
        return partnerRepositoryPort.findByPartnerType(partnerType);
    }

    @Override
    public Partner updatePartner(String id, PartnerDTO partnerDTO) {

       Optional <Partner> existing = partnerRepositoryPort.findById(id);

       if (existing.isPresent()) {
           Partner updated = Partner.builder()
                   .idPartner(existing.get().getIdPartner())
                   .name(partnerDTO.getName())
                   .email(partnerDTO.getEmail())
                   .phoneNumber(partnerDTO.getPhoneNumber())
                   .country(partnerDTO.getCountry())
                   .address(partnerDTO.getAddress())
                   .iban(partnerDTO.getIban())
                   .partnerType(existing.get().getPartnerType())
                   .taxRegistrationNumber(existing.get().getTaxRegistrationNumber())
                   .rne(existing.get().getRne())
                   .patente(existing.get().getPatente())
                   .contract(existing.get().getContract())

                   .build();
           return partnerRepositoryPort.save(updated);
       }
       else {
           throw new EntityNotFoundException("Partner with id " + id + " not found");
       }
    }

}
