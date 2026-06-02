package com.example.billingservice.infrastructure.out.persistance;


import com.example.billingservice.application.ports.out.SupplierRepositoryPort;
import com.example.billingservice.domain.enums.AuditEventTrigger;
import com.example.billingservice.domain.enums.AuditType;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Partner;

import com.example.billingservice.infrastructure.out.persistance.dto.PartnerDetailsDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.UpdatePartnerDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.AuditLogEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.CustomerEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.SupplierEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.PartnerMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.AuditLogRepository;
import com.example.billingservice.infrastructure.out.persistance.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.function.SupplierUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SupplierPersistanceAdapter implements SupplierRepositoryPort {

    private final SupplierRepository supplierRepository;
    private final PartnerMapper partnerMapper;
    private final AuditLogRepository auditLogRepository;

    @Override
    public Partner saveSupplier(Partner partner) throws DataIntegrityViolationException{

        SupplierEntity entity = (SupplierEntity) partnerMapper.toEntity(partner);
        SupplierEntity savedEntity = (SupplierEntity) supplierRepository.save(entity);
        AuditLogEntity audit = new AuditLogEntity();
        audit.setAuditEventType(AuditType.CREATED);
        audit.setEntityName("Partner");
        audit.setTriggeredBy("user");
        audit.setAuditEventTrigger(AuditEventTrigger.USER);
        audit.setEntityId(savedEntity.getIdPartner());
        audit.setDescription("Ajout d'un nouveau partenaire");
        audit.setEventDate(new Date());
        audit.setPartner(savedEntity);
        auditLogRepository.save(audit);

        return partnerMapper.toDomain(supplierRepository.save(entity), PartnerType.SUPPLIER) ;

    }

    @Override
    public Optional<Partner> findSupplierById(String id) {
        try
        {
            return supplierRepository.findById(UUID.fromString(id))
                    .map(p-> partnerMapper.toDomain(p,PartnerType.SUPPLIER))
                    .or(() -> { throw BillingException.notFound("Fournisseur", id); });
        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest("Invalid UUID "+id);
        }
    }

    @Override
    public Optional<Partner> getSupplierById(UUID idSupplier) {
        try
        {
            return supplierRepository.findById(idSupplier)
                    .map(p-> partnerMapper.toDomain(p,PartnerType.SUPPLIER))
                    .or(() -> { throw BillingException.notFound("Fournisseur", String.valueOf(idSupplier)); });
        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest("Invalid UUID "+idSupplier);
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
    public PartnerItemDTO getByEmail(String email) {
        SupplierEntity supplierEntity = supplierRepository.getSupplierEntityByEmail(email);
        Partner partner = partnerMapper.toDomain(supplierEntity,PartnerType.SUPPLIER);
        return partnerMapper.toItemDTO(partner);
    }

    @Override
    public boolean existsByName(String name) {
        return supplierRepository.existsByName(name);
    }

    @Override
    public Optional<Partner> findSupplierByName(String name) {
        try
        {return supplierRepository.findByPartnerName(name)
                    .map(p-> partnerMapper.toDomain(p,PartnerType.SUPPLIER))
                    .or(() -> { throw BillingException.notFound("Fournisseur", name); });
        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest( "bad request"+ ex);
        }
    }

    @Override
    public Optional<Partner> findSupplierByEmail(String email) {
        try
        {return supplierRepository.findByEmail(email)
                .map(p-> partnerMapper.toDomain(p,PartnerType.SUPPLIER))
                .or(() -> { throw BillingException.notFound("Fournisseur", email); });
        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest( "bad request"+ ex);
        }
    }

    @Override
    public boolean existsByIban(String iban) {
        return supplierRepository.existsByIban(iban);
    }



    @Override
    public Page<PartnerItemDTO> findAllSuppliers(String keyword, String Country, int page) {
        try {

            PageRequest pageRequest = PageRequest.of(page, 5, Sort.by("partnerName").ascending());
            Page<SupplierEntity> entities = supplierRepository.findSuppliers(keyword,Country,pageRequest);

            List<PartnerItemDTO> partners = entities.getContent()
                    .stream()
                    .map(p->partnerMapper.toDomain(p,PartnerType.SUPPLIER))
                    .map(partnerMapper::toItemDTO)
                    .collect(Collectors.toList());

            return new PageImpl<>(partners, pageRequest, entities.getTotalElements());

        } catch (DataAccessException ex) {
            throw BillingException.internalError("Failed to fetch suppliers: " + ex.getMessage());
        }
    }

    @Override
    public Partner updateSupplier(String id , UpdatePartnerDTO partner) throws DataIntegrityViolationException {

        SupplierEntity managedEntity = supplierRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> BillingException.notFound("Fournisseur", id));
        SupplierEntity entity = (SupplierEntity) partnerMapper.updateEntity(partner,managedEntity);
        Partner updatedPartner =  partnerMapper.toDomain(supplierRepository.save(entity),PartnerType.SUPPLIER);
        AuditLogEntity audit = new AuditLogEntity();
        audit.setAuditEventType(AuditType.CREATED);
        audit.setEntityName("Partner");
        audit.setTriggeredBy("user");
        audit.setAuditEventTrigger(AuditEventTrigger.USER);
        audit.setEntityId(entity.getIdPartner());
        audit.setDescription("Modification d'un partenaire");
        audit.setEventDate(new Date());
        audit.setPartner(entity);
        auditLogRepository.save(audit);
        return updatedPartner;
    }

    @Override
    public void deleteSupplierById(String id) {

        try {
            UUID uuid = UUID.fromString(id);
            if (!supplierRepository.existsById(uuid)) {
                throw BillingException.notFound("Supplier", id);
            }
            Optional<SupplierEntity> partnerToDelete = supplierRepository.findById(UUID.fromString(id));
            AuditLogEntity audit = new AuditLogEntity();
            audit.setAuditEventType(AuditType.DELETED);
            audit.setEntityName("Partner");
            audit.setTriggeredBy("user");
            audit.setAuditEventTrigger(AuditEventTrigger.USER);
            audit.setEntityId(partnerToDelete.get().getIdPartner());
            audit.setDescription("Suppression d'un partenaire");
            audit.setEventDate(new Date());
            auditLogRepository.save(audit);
            supplierRepository.deleteById(uuid);

        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest("Invalid UUID format: " + id);
        }

    }

    @Override
    public void updateSupplierStatus(String idSupplier, Boolean status) {
        SupplierEntity entity;
        try {
            UUID supplierId = UUID.fromString(idSupplier);
            entity = supplierRepository.findById(supplierId)
                    .orElseThrow(() -> BillingException.notFound("Fournisseur", idSupplier));
            entity.setActive(status);
            supplierRepository.save(entity);
            AuditLogEntity audit = new AuditLogEntity();
            audit.setAuditEventType(AuditType.UPDATED);
            audit.setEntityName("Partner");
            audit.setTriggeredBy("user");
            audit.setAuditEventTrigger(AuditEventTrigger.USER);
            audit.setEntityId(entity.getIdPartner());
            if(status) {
                audit.setDescription("Activation du fournisseur");
            }
            else {
                audit.setDescription("Désactivation du fournisseur");
            }
            audit.setPartner(entity);
            audit.setEventDate(new Date());
            auditLogRepository.save(audit);

        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest("UUID invalide : " + idSupplier);
        }
    }
}
