package com.example.billingservice.application.ports.out;

import com.example.billingservice.domain.model.Document;
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

    PartnerDetailsDTO addDocumentToClient(UUID idClient, Document document);

    PartnerDetailsDTO getSupplierDetailsById(UUID idSupplier);
    Optional<Partner> getSupplierById(UUID idSupplier);

    boolean existsByIdPartner(UUID idPartner);

    boolean existsByTaxRegistrationNumber(String taxRegistrationNumber);

    boolean existsByEmail(String email);

    PartnerItemDTO getByEmail(String email);

    boolean existsByName(String name);

    boolean existsByCompanyName(String companyName);

    boolean existsByIbanAndIdPartnerNot(String iban, UUID idPartner);

    boolean existsByEmailAndIdPartnerNot(String email, UUID idPartner);

    boolean existsByCompanyNameAndIdPartnerNot(String companyName, UUID idPartner);

    boolean existsByTaxRegistrationNumberAndIdPartnerNot(String taxRegistrationNumber, UUID idPartner);

    Optional<Partner> findSupplierByName(String name);

    Optional<Partner> findSupplierByEmail(String email);

    boolean existsByIban(String email);

    Page<PartnerItemDTO> findAllSuppliers(String keyword , String Country , int page);

    Partner updateSupplier (String id , UpdatePartnerDTO partner) throws DataIntegrityViolationException;

    void deleteSupplierById(String id);

    void updateSupplierStatus(String idSupplier, Boolean status);

    List<PartnerSummaryDTO> getSummarySuppliers(String keyword , String Country);
}
