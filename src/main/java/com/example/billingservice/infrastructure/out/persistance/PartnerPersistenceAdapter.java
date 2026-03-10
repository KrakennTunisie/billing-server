package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.PartnerRepositoryPort;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.entity.PartnerEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.PartnerMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.JpaPartnerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PartnerPersistenceAdapter implements PartnerRepositoryPort {

    private final JpaPartnerRepository jpaPartnerRepository;
    private final PartnerMapper partnerMapper;
    @Override
    public Partner save(Partner partner) {
        PartnerEntity entity = partnerMapper.toEntity(partner);

        return partnerMapper.toDomain(jpaPartnerRepository.save(entity)) ;
    }

    @Override
    public Optional<Partner> findById(String id) {
         PartnerEntity entity =  jpaPartnerRepository.findById(UUID.fromString(id)).get();
           return Optional.ofNullable(partnerMapper.toDomain(entity));
    }

    @Override
    public List<Partner> findAll() {
        List <PartnerEntity> entities =  jpaPartnerRepository.findAll();
        return entities.stream().map(partnerMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
      jpaPartnerRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public boolean existsById(String id) {

       return  jpaPartnerRepository.existsById(UUID.fromString(id));
    }

    @Override
    public Partner findByName(String name) {
        return jpaPartnerRepository.findByName(name)
                .map(partnerMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Partner with name " + name + " not found"));
    }

    @Override
    public Partner findByEmail(String email) {
        if(jpaPartnerRepository.findByEmail(email).isPresent())
        {
            return partnerMapper.toDomain(jpaPartnerRepository.findByEmail(email).get());
        }
        return null;
    }

    @Override
    public Partner findByTaxRegistrationNumber(String taxRegistrationNumber) {
        if(jpaPartnerRepository.findByTaxRegistrationNumber(taxRegistrationNumber).isPresent())
        {
            return partnerMapper.toDomain(jpaPartnerRepository.findByTaxRegistrationNumber(taxRegistrationNumber).get());
        }
        return null;
    }

    @Override
    public List<Partner> findByPartnerType(PartnerType partnerType) {
        List <PartnerEntity> entities =  jpaPartnerRepository.findByPartnerType(partnerType);
        return entities.stream().map(partnerMapper::toDomain).collect(Collectors.toList());
    }

}
