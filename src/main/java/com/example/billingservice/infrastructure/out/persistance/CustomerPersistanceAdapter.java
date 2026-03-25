package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.CustomerRepositoryPort;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.exceptions.DatabaseException;
import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.entity.CustomerEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.PartnerMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
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
        return partnerMapper.toDomain(customerRepository.save(entity)) ;

    }

    @Override
    public Optional<Partner> findCustomerById(String id) {
        try
        {
            return customerRepository.findById(UUID.fromString(id))
                    .map(partnerMapper::toDomain).or(() -> { throw BillingException.notFound("Client", id); });
        } catch (IllegalArgumentException ex) {
            throw BillingException.badRequest("UUID Invalid"+id);
        }

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
    public Page<Partner> findAllCustomers(String keyword , String Country ,int page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("name").ascending());
        Page<CustomerEntity> entities = customerRepository.findCustomers(keyword,Country,pageRequest);

        List<Partner> partners = entities.getContent()
                .stream()
                .map(partnerMapper::toDomain)
                .collect(Collectors.toList());

        return new PageImpl<>(partners, pageRequest, entities.getTotalElements());

    }

    @Override
    public Partner updateCustomer(Partner partner) {
        try{
            CustomerEntity entity = (CustomerEntity) partnerMapper.toEntity(partner);
            return partnerMapper.toDomain(customerRepository.save(entity));

        } catch (Exception ex) {
            throw BillingException.internalError("Failed to save customer "+ex.getMessage());
        }
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
