package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerItemDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface SupplierRepositoryPort {

    Partner saveSupplier  (Partner partner) throws DataIntegrityViolationException;
    Optional<Partner> findSupplierById(String id);

    boolean existsByIdPartner(UUID idPartner);

    boolean existsByTaxRegistrationNumber(String taxRegistrationNumber);

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    boolean existsByIban(String email);
    Page<PartnerItemDTO> findAllSuppliers(String keyword , String Country , int page);
    Partner updateSupplier (Partner partner) throws DataIntegrityViolationException;
    void deleteSupplierById(String id);
}
