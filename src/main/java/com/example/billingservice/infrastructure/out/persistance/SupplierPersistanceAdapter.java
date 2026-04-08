package com.example.billingservice.infrastructure.out.persistance;


import com.example.billingservice.application.ports.out.SupplierRepositoryPort;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Partner;

import com.example.billingservice.infrastructure.out.persistance.dto.PartnerItemDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.SupplierEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.PartnerMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
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
    public Partner saveSupplier(Partner partner) throws DataIntegrityViolationException{

        SupplierEntity entity = (SupplierEntity) partnerMapper.toEntity(partner);

        return partnerMapper.toDomain(supplierRepository.save(entity), PartnerType.SUPPLIER) ;

    }

    @Override
    public Optional<Partner> findSupplierById(String id) {
        try
        {
            return supplierRepository.findById(UUID.fromString(id))
                    .map(p-> partnerMapper.toDomain(p, PartnerType.SUPPLIER))
                    .or(() -> { throw BillingException.notFound("Fournisseur", id); });
        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest("Invalid UUID "+id);
        }
    }

    @Override
    public boolean existsByIdPartner(UUID idPartner) {
        return supplierRepository.existsByIdPartner(idPartner);
    }

    @Override
    public boolean existsByTaxRegistrationNumber(String taxRegistrationNumber) {
        return supplierRepository.existsByTaxRegistrationNumber(taxRegistrationNumber);
    }

    @Override
    public boolean existsByEmail(String email) {
        return supplierRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByIban(String iban) {
        return supplierRepository.existsByIban(iban);
    }



    @Override
    public Page<PartnerItemDTO> findAllSuppliers(String keyword, String Country, int page) {
        try {

            PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("name").ascending());
            Page<SupplierEntity> entities = supplierRepository.findSuppliers(keyword,Country,pageRequest);

            List<PartnerItemDTO> partners = entities.getContent()
                    .stream()
                    .map(partnerMapper::toItemDTO)
                    .collect(Collectors.toList());

            return new PageImpl<>(partners, pageRequest, entities.getTotalElements());

        } catch (DataAccessException ex) {
            throw BillingException.internalError("Failed to fetch suppliers: " + ex.getMessage());
        }
    }

    @Override
    public Partner updateSupplier(Partner partner) throws DataIntegrityViolationException {

            SupplierEntity entity = (SupplierEntity) partnerMapper.toEntity(partner);

            SupplierEntity savedSupplier = supplierRepository.save(entity);

            return partnerMapper.toDomain(savedSupplier, PartnerType.SUPPLIER);


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
