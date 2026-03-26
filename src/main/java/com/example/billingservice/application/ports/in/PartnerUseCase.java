package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerForm;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.UpdatePartnerDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.Optional;

public interface PartnerUseCase {


    /****** Supplier *****/

    Partner createSupplier(PartnerForm partner) throws IOException, DataIntegrityViolationException;

    Optional<Partner> getSupplierById(String id);
    boolean supplierExistsByRegistrationNumber(String taxRegistrationNumber);
    boolean supplierExistsByEmail(String email);
    boolean supplierExistsByIban(String iban);

    Page<PartnerItemDTO> getAllSuppliers(String keyword , String Country , int page);

    Partner updateSupplier (String id , UpdatePartnerDTO command);

    void deleteSupplier(String id);


    /**** CUSTOMER ****/

    Optional<Partner> createCustomer(PartnerForm partner) throws IOException;

    Optional<Partner> findCustomerById(String id);

    boolean customerExistsByRegistrationNumber(String taxRegistrationNumber);
    boolean customerExistsByEmail(String email);
    boolean customerExistsByIban(String iban);

    Page<PartnerItemDTO> getAllCustomers(String keyword , String Country ,int page);

    void deleteCustomerById(String id);

    Partner updateCustomer(String id, UpdatePartnerDTO partner);






}
