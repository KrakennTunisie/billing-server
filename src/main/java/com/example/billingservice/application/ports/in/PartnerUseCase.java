package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerForm;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public interface PartnerUseCase {


    /****** Supplier *****/

    Optional<Partner> createSupplier(PartnerForm partner) throws IOException, DataIntegrityViolationException;

    Optional<Partner> getSupplierById(String id);
    boolean supplierExistsByRegistrationNumber(String taxRegistrationNumber);
    boolean supplierExistsByEmail(String email);
    boolean supplierExistsByIban(String iban);

    Page<Partner> getAllSuppliers(String keyword , String Country ,int page);

    Partner updateSupplier (String id , PartnerDTO command);

    void deleteSupplier(String id);


    /**** CUSTOMER ****/

    Optional<Partner> createCustomer(PartnerForm partner) throws IOException;

    Optional<Partner> findCustomerById(String id);

    boolean customerExistsByRegistrationNumber(String taxRegistrationNumber);
    boolean customerExistsByEmail(String email);
    boolean customerExistsByIban(String iban);

    Page<Partner> getAllCustomers(String keyword , String Country ,int page);

    void deleteCustomerById(String id);

    Partner updateCustomer(String id,PartnerDTO partner);

    /*** Common ***/





}
