package com.example.billingservice.infrastructure.out.persistance;


import com.example.billingservice.application.ports.out.SupplierRepositoryPort;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Partner;

import com.example.billingservice.infrastructure.out.persistance.entity.CustomerEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.SupplierEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.PartnerMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SupplierPersistanceAdapter implements SupplierRepositoryPort {

    private final SupplierRepository supplierRepository;
    private final PartnerMapper partnerMapper;

    @Override
    public Partner saveSupplier(Partner partner) {
        supplierRepository.findByTaxRegistrationNumber(partner.getTaxRegistrationNumber()).ifPresent(p-> {
            throw BillingException.alreadyExists("Supplier","taxRegistrationNumber",partner.getTaxRegistrationNumber());
        });
        try{
            SupplierEntity entity = (SupplierEntity) partnerMapper.toEntity(partner);
            return partnerMapper.toDomain(supplierRepository.save(entity)) ;
        } catch (DataAccessException ex) {
            throw  BillingException.internalError("Failed to save Supplier "+ex.getMessage());
        }
    }

    @Override
    public Optional<Partner> findSupplierById(String id) {
        try
        {
            return supplierRepository.findById(UUID.fromString(id))
                    .map(partnerMapper::toDomain).or(() -> { throw BillingException.notFound("Supplier", id); });
        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest("Invalid UUID "+id);
        }
    }

    @Override
    public Page<Partner> findAllSuppliers(String keyword, String Country, int page) {
        try {
            PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("name").ascending());
            Page<SupplierEntity> entities = supplierRepository.findSuppliers(keyword,Country,pageRequest);

            List<Partner> partners = entities.getContent()
                    .stream()
                    .map(partnerMapper::toDomain)
                    .collect(Collectors.toList());

            return new PageImpl<>(partners, pageRequest, entities.getTotalElements());

        } catch (DataAccessException ex) {
            throw BillingException.internalError("Failed to fetch suppliers: " + ex.getMessage());
        }
    }

    @Override
    public Partner updateSupplier(Partner partner) {
        try{
            SupplierEntity entity = (SupplierEntity) partnerMapper.toEntity(partner);
            return partnerMapper.toDomain(supplierRepository.save(entity));

        } catch (Exception ex) {
            throw BillingException.internalError("Failed to save Supplier "+ex.getMessage());
        }
    }

    @Override
    public void deleteSupplierById(String id) {

        try {
            UUID uuid = UUID.fromString(id);

            if (!supplierRepository.existsById(uuid)) {
                throw BillingException.notFound("Supplier", id);
            }

            supplierRepository.deleteById(uuid);

        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest("Invalid UUID format: " + id);
        }

    }
}
