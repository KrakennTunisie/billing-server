package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.model.Partner;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CustomerRepositoryPort {
    Partner saveCustomer (Partner partner);
    Optional<Partner> findCustomerById(String id);
    Page<Partner> findAllCustomers(String keyword , String Country ,int page);
    Partner updateCustomer (Partner partner);
    void deleteCustomerById(String id);
}
