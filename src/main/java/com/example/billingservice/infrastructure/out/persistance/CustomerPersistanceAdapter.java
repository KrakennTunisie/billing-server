package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.CustomerRepositoryPort;
import com.example.billingservice.domain.enums.AuditEventTrigger;
import com.example.billingservice.domain.enums.AuditType;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerDetailsDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerSummaryDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.UpdatePartnerDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.AuditLogEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.CustomerEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.PartnerMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.AuditLogRepository;
import com.example.billingservice.infrastructure.out.persistance.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerPersistanceAdapter implements CustomerRepositoryPort {

    private final PartnerMapper partnerMapper;
    private final CustomerRepository customerRepository;
    private final AuditLogRepository auditLogRepository;

    @Override
    public Partner saveCustomer(Partner partner) {

        CustomerEntity entity = (CustomerEntity) partnerMapper.toEntity(partner);
        CustomerEntity savedEntity = (CustomerEntity) customerRepository.save(entity);

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

        return partnerMapper.toDomain(savedEntity,PartnerType.CLIENT);
    }

    @Override
    public PartnerDetailsDTO getClientDetailsById(UUID idClient) {
        CustomerEntity customerEntity = customerRepository.getReferenceById(idClient);
        return partnerMapper.toDetailsDTO(partnerMapper.toDomain(customerEntity, PartnerType.CLIENT));
    }

    @Override
    public Optional<Partner> findCustomerById(String id) {
        try
        {
            return customerRepository.findById(UUID.fromString(id))
                    .map(p -> partnerMapper.toDomain(p,PartnerType.CLIENT))
                    .or(() -> { throw BillingException.notFound("Client", id); });
        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest("UUID Invalid"+id);
        }

    }

    @Override
    public Optional<Partner> findCustomerByEmail(String email) {
        try
        {
            return customerRepository.findByEmail(email)
                    .map(p -> partnerMapper.toDomain(p,PartnerType.CLIENT))
                    .or(() -> { throw BillingException.notFound("Client", email); });
        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest("email Invalid"+email);
        }
    }

    @Override
    public boolean existsByIdPartner(UUID idPartner) {
        return customerRepository.existsByIdPartner(idPartner);
    }

    @Override
    public boolean existsByTaxRegistrationNumber(String taxRegistrationNumber) {
        return customerRepository.existsByTaxRegistrationNumber(taxRegistrationNumber);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByIban(String iban) {
        return customerRepository.existsByIban(iban);
    }

    @Override
    public boolean existsByName(String name) {
        return customerRepository.existsByPartnerName(name);
    }

    @Override
    public Page<PartnerItemDTO> findAllCustomers(String keyword , String Country ,int page) {
        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by("partnerName").ascending());
        Page<CustomerEntity> entities = customerRepository.findCustomers(keyword,Country,pageRequest);

        List<PartnerItemDTO> partners = entities.getContent()
                .stream()
                .map(p-> partnerMapper.toDomain(p,PartnerType.CLIENT))
                .map(partnerMapper::toItemDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(partners, pageRequest, entities.getTotalElements());

    }

    @Override
    public List<PartnerSummaryDTO> getSummaryClients(String keyword, String Country) {
        List<CustomerEntity> customerEntities= customerRepository.getCustomers(keyword, Country);
        return customerEntities.stream()
                .map(entity->partnerMapper.toDomain(entity,PartnerType.CLIENT))
                .map(partnerMapper::toSummaryDTO)
                .toList();
    }

    @Override
    public Partner updateCustomer(String id,UpdatePartnerDTO partner) throws DataIntegrityViolationException {
        CustomerEntity managedEntity = customerRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> BillingException.notFound("Client", id));
            CustomerEntity entity = (CustomerEntity) partnerMapper.updateEntity(partner,managedEntity);
            Partner savedPartner =  partnerMapper.toDomain(customerRepository.save(entity),PartnerType.CLIENT);
        AuditLogEntity audit = new AuditLogEntity();
        audit.setAuditEventType(AuditType.UPDATED);
        audit.setEntityName("Partner");
        audit.setTriggeredBy("user");
        audit.setAuditEventTrigger(AuditEventTrigger.USER);
        audit.setEntityId(entity.getIdPartner());
        audit.setDescription("Modification d'un  partenaire");
        audit.setEventDate(new Date());
        audit.setPartner(entity);
        auditLogRepository.save(audit);
        return  savedPartner;
    }

    @Override
    public void deleteCustomerById(String id) {
        try {
            UUID uuid = UUID.fromString(id);

            if (!customerRepository.existsById(uuid)) {
                throw BillingException.notFound("Customer", id);
            }
            Optional<CustomerEntity> partnerToDelete = customerRepository.findById(UUID.fromString(id));
            AuditLogEntity audit = new AuditLogEntity();
            audit.setAuditEventType(AuditType.DELETED);
            audit.setEntityName("Partner");
            audit.setTriggeredBy("user");
            audit.setAuditEventTrigger(AuditEventTrigger.USER);
            audit.setEntityId(partnerToDelete.get().getIdPartner());
            audit.setDescription("Suppression d'un partenaire partenaire");
            audit.setEventDate(new Date());
            auditLogRepository.save(audit);
            customerRepository.deleteById(uuid);

        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest("Invalid UUID format: " + id);
        }
    }

    @Override
    public void updateCustomerStatus(String idClient ,Boolean statuts) {
        CustomerEntity entity;
        try {
            UUID customerId = UUID.fromString(idClient);

            entity = customerRepository.findById(customerId)
                    .orElseThrow(() -> BillingException.notFound("Client", idClient));
            entity.setActive(statuts);
            customerRepository.save(entity);
            AuditLogEntity audit = new AuditLogEntity();
            audit.setAuditEventType(AuditType.UPDATED);
            audit.setEntityName("Partner");
            audit.setTriggeredBy("user");
            audit.setAuditEventTrigger(AuditEventTrigger.USER);
            audit.setEntityId(entity.getIdPartner());
            if(statuts) {
                audit.setDescription("Activation du client");
            }
            else {
                audit.setDescription("Désactivation du client");
            }
            audit.setPartner(entity);
            audit.setEventDate(new Date());
            auditLogRepository.save(audit);

        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest("UUID invalide : " + idClient);
        }
    }
}
