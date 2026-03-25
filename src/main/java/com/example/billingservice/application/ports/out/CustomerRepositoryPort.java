package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.Partner;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CustomerRepositoryPort {
    Partner saveCustomer (Partner partner);
    Optional<Partner> findCustomerById(String id);
    boolean existsByTaxRegistrationNumber(String taxRegistrationNumber);

    boolean existsByEmail(String email);

    boolean existsByIban(String iban);
    Page<Partner> findAllCustomers(String keyword , String Country ,int page);
    Partner updateCustomer (Partner partner);
    void deleteCustomerById(String id);
}
