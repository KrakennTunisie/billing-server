package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerForm;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.Optional;

public interface PartnerUseCase {


    /****** Supplier *****/

    Optional<Partner> createSupplier(PartnerForm partner) throws IOException;
    Optional<Partner> getSupplierById(String id);
    boolean existsByRegistrationNumbe(String taxRegistrationNumber);
    Page<Partner> getAllSuppliers(String keyword , String Country ,int page);
    Partner updateSupplier (String id , PartnerDTO command);
    void deleteSupplier(String id);


    /**** CUSTOMER ****/

    Optional<Partner> createCustomer(PartnerForm partner) throws IOException;
    Page<Partner> getAllCustomers(String keyword , String Country ,int page);
    Optional<Partner> findCustomerById(String id);
    void deleteCustomerById(String id);
    Partner updateCustomer(String id,PartnerDTO partner);


}
