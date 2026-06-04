package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupplierRepositoryPort {

    Partner saveSupplier  (Partner partner) throws DataIntegrityViolationException;
    Optional<Partner> findSupplierById(String id);

    Optional<Partner> getSupplierById(UUID idSupplier);

    boolean existsByIdPartner(UUID idPartner);

    boolean existsByTaxRegistrationNumber(String taxRegistrationNumber);

    boolean existsByEmail(String email);

    PartnerItemDTO getByEmail(String email);

    boolean existsByName(String name);

    Optional<Partner> findSupplierByName(String name);

    Optional<Partner> findSupplierByEmail(String email);

    boolean existsByIban(String email);

    Page<PartnerItemDTO> findAllSuppliers(String keyword , String Country , int page);

    Partner updateSupplier (String id , UpdatePartnerDTO partner) throws DataIntegrityViolationException;

    void deleteSupplierById(String id);

    void updateSupplierStatus(String idSupplier, Boolean status);

    List<PartnerSummaryDTO> getSummarySuppliers(String keyword , String Country);
}
