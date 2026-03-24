package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.Partner;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface SupplierRepositoryPort {

    Partner saveSupplier (Partner partner);
    Optional<Partner> findSupplierById(String id);
    boolean existsByRegistrationNumbe(String registrationNumber);
    Page<Partner> findAllSuppliers(String keyword , String Country , int page);
    Partner updateSupplier (Partner partner);
    void deleteSupplierById(String id);
}
