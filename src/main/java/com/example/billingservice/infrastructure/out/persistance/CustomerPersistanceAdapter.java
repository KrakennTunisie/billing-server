package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.CustomerRepositoryPort;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerSummaryDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.CustomerEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.PartnerMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
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
public class CustomerPersistanceAdapter implements CustomerRepositoryPort {

    private final PartnerMapper partnerMapper;
    private final CustomerRepository customerRepository;

    @Override
    public Partner saveCustomer(Partner partner) {

        CustomerEntity entity = (CustomerEntity) partnerMapper.toEntity(partner);
        return partnerMapper.toDomain(customerRepository.save(entity), PartnerType.CLIENT) ;

    }

    @Override
    public Optional<Partner> findCustomerById(String id) {
        try
        {
            return customerRepository.findById(UUID.fromString(id))
                    .map(p -> partnerMapper.toDomain(p, PartnerType.CLIENT)).or(() -> { throw BillingException.notFound("Client", id); });
        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest("UUID Invalid"+id);
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
        return customerRepository.existsByName(name);
    }

    @Override
    public Page<PartnerItemDTO> findAllCustomers(String keyword , String Country ,int page) {
        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by("name").ascending());
        Page<CustomerEntity> entities = customerRepository.findCustomers(keyword,Country,pageRequest);

        List<PartnerItemDTO> partners = entities.getContent()
                .stream()
                .map(p-> partnerMapper.toDomain(p, PartnerType.CLIENT))
                .map(partnerMapper::toItemDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(partners, pageRequest, entities.getTotalElements());

    }

    @Override
    public List<PartnerSummaryDTO> getSummaryClients(String keyword, String Country) {
        List<CustomerEntity> customerEntities= customerRepository.getCustomers(keyword, Country);
        return customerEntities.stream()
                .map(entity->partnerMapper.toDomain(entity, PartnerType.CLIENT))
                .map(partnerMapper::toSummaryDTO)
                .toList();
    }

    @Override
    public Partner updateCustomer(Partner partner) throws DataIntegrityViolationException {
            CustomerEntity entity = (CustomerEntity) partnerMapper.toEntity(partner);
            return partnerMapper.toDomain(customerRepository.save(entity), PartnerType.CLIENT);

    }

    @Override
    public void deleteCustomerById(String id) {
        try {
            UUID uuid = UUID.fromString(id);

            if (!customerRepository.existsById(uuid)) {
                throw BillingException.notFound("Customer", id);
            }

            customerRepository.deleteById(uuid);

        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest("Invalid UUID format: " + id);
        }
    }
}
