package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerItemDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryPort {
    Partner saveCustomer (Partner partner);
    Optional<Partner> findCustomerById(String id);

    boolean existsByIdPartner(UUID idPartner);

    boolean existsByTaxRegistrationNumber(String taxRegistrationNumber);

    boolean existsByEmail(String email);

    boolean existsByIban(String iban);

    Page<PartnerItemDTO> findAllCustomers(String keyword , String Country , int page);

    Partner updateCustomer (Partner partner);

    void deleteCustomerById(String id);
}
