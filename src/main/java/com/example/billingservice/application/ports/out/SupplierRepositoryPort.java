package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerItemDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

import java.sql.SQLException;
import java.util.Optional;

public interface SupplierRepositoryPort {

    Partner saveSupplier  (Partner partner) throws DataIntegrityViolationException;
    Optional<Partner> findSupplierById(String id);
    boolean existsByTaxRegistrationNumber(String taxRegistrationNumber);

    boolean existsByEmail(String email);

    boolean existsByIban(String email);
    Page<PartnerItemDTO> findAllSuppliers(String keyword , String Country , int page);
    Partner updateSupplier (Partner partner);
    void deleteSupplierById(String id);
}
