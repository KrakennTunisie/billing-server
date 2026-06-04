package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PartnerUseCase {


    /****** Supplier *****/

    Partner createSupplier(PartnerForm partner) throws IOException, DataIntegrityViolationException;

    Optional<Partner> getSupplierById(String id);

    Optional<Partner> getSupplierDetailsById(String idSupplier);

    PartnerItemDTO getSupplierByEmail(String email);
    boolean supplierExistsByIdPartner(UUID idPartner);
    boolean supplierExistsByRegistrationNumber(String taxRegistrationNumber);
    boolean supplierExistsByEmail(String email);
    Optional<Partner> findSupplierExistsByEmail(String email);
    boolean supplierExistsByIban(String iban);
    Optional<Partner> getSupplierByName(String name);

    Page<PartnerItemDTO> getAllSuppliers(String keyword , String Country , int page);
    boolean existsBySupplierName(String name);

    Partner updateSupplier (String id , UpdatePartnerDTO command) throws DataIntegrityViolationException;

    void deleteSupplier(String id);
    void  updateSupplierStatus(String idSupplier, Boolean status);
    List<PartnerSummaryDTO> getSummarySuppliers(String keyword , String Country );



    /**** CUSTOMER ****/

    Optional<Partner> createCustomer(PartnerForm partner) throws IOException;

    Optional<Partner> findCustomerById(String id);

    Optional<Partner> findCustomerByEmail(String email);


    Optional<Partner> getClientDetailsById(String idClient);

    boolean customerExistsByIdPartner(UUID idPartner);

    boolean customerExistsByRegistrationNumber(String taxRegistrationNumber);
    boolean customerExistsByEmail(String email);
    boolean customerExistsByIban(String iban);

    Page<PartnerItemDTO> getAllCustomers(String keyword , String Country ,int page);

    List<PartnerSummaryDTO> getSummaryClients(String keyword , String Country );

    void deleteCustomerById(String id);

    Partner updateCustomer(String id, UpdatePartnerDTO partner) throws DataIntegrityViolationException;
    void  updateCustomerStatus(String idClient, Boolean status);


}
