package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerForm;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerSummaryDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.UpdatePartnerDTO;
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
    boolean supplierExistsByIdPartner(UUID idPartner);
    boolean supplierExistsByRegistrationNumber(String taxRegistrationNumber);
    boolean supplierExistsByEmail(String email);
    boolean supplierExistsByIban(String iban);

    Page<PartnerItemDTO> getAllSuppliers(String keyword , String Country , int page);


    Partner updateSupplier (String id , UpdatePartnerDTO command) throws DataIntegrityViolationException;

    void deleteSupplier(String id);


    /**** CUSTOMER ****/

    Optional<Partner> createCustomer(PartnerForm partner) throws IOException;

    Optional<Partner> findCustomerById(String id);

    boolean customerExistsByIdPartner(UUID idPartner);

    boolean customerExistsByRegistrationNumber(String taxRegistrationNumber);
    boolean customerExistsByEmail(String email);
    boolean customerExistsByIban(String iban);

    Page<PartnerItemDTO> getAllCustomers(String keyword , String Country ,int page);

    List<PartnerSummaryDTO> getSummaryClients(String keyword , String Country );

    void deleteCustomerById(String id);

    Partner updateCustomer(String id, UpdatePartnerDTO partner) throws DataIntegrityViolationException;






}
