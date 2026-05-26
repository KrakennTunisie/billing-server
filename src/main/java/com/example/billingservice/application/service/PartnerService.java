package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.out.CustomerRepositoryPort;
import com.example.billingservice.application.ports.out.SupplierRepositoryPort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.mapper.PartnerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartnerService implements PartnerUseCase  {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final SupplierRepositoryPort supplierRepositoryPort;
    private final UploadDocumentService uploadDocumentService;
    private final PartnerMapper partnerMapper;



    /********* SUPPLIER ********/

    @Override
    public Partner createSupplier(PartnerForm partner) throws IOException, DataIntegrityViolationException {
        if(!Objects.equals(partner.getPartnerType(), PartnerType.SUPPLIER)){
            throw BillingException
                    .badRequest("Le Type est inadéquat");
        }
        if (this.supplierRepositoryPort.existsByName(partner.getCompanyName())){
            throw BillingException
                    .alreadyExists(
                            "Fournisseur",
                            "Nom",
                            partner.getCompanyName());
        }
        if (this.supplierExistsByRegistrationNumber(partner.getTaxRegistrationNumber())){
            throw BillingException
                    .alreadyExists(
                            "Fournisseur",
                            "Tax Registration Number",
                            partner.getTaxRegistrationNumber());
        }
        if (this.supplierExistsByEmail(partner.getEmail())){
            throw BillingException
                    .alreadyExists(
                            "Fournisseur",
                            "Email",
                            partner.getEmail());
        }
        if (this.supplierExistsByIban(partner.getIban())){
            throw BillingException
                    .alreadyExists(
                            "Fournisseur",
                            "Iban",
                            partner.getIban());
        }
        Partner  partnerModel =partnerMapper.createPartnerFromDTO(partner);
        return supplierRepositoryPort.saveSupplier(partnerModel);
    }

    @Override
    public Optional<Partner> getSupplierById(String id) {
        return supplierRepositoryPort.findSupplierById(id);
    }

    @Override
    public PartnerItemDTO getSupplierByEmail(String email) {
        if(!supplierRepositoryPort.existsByEmail(email)){
            throw BillingException.notFound("Fournisseur", email);
        }
        return supplierRepositoryPort.getByEmail(email);
    }

    @Override
    public boolean supplierExistsByIdPartner(UUID idPartner) {
        return supplierRepositoryPort.existsByIdPartner(idPartner);
    }

    @Override
    public boolean supplierExistsByRegistrationNumber(String taxRegistrationNumber) {
        return supplierRepositoryPort.existsByTaxRegistrationNumber(taxRegistrationNumber);
    }

    @Override
    public boolean supplierExistsByEmail(String email) {
        return supplierRepositoryPort.existsByEmail(email);
    }

    @Override
    public boolean supplierExistsByIban(String iban) {
        return supplierRepositoryPort.existsByIban(iban);
    }

    @Override
    public Optional<Partner> getSupplierByName(String name) {
        return supplierRepositoryPort.findSupplierByName(name);
    }

    @Override
    public Page<PartnerItemDTO> getAllSuppliers(String keyword , String Country , int page) {
        return  supplierRepositoryPort
                .findAllSuppliers(keyword, Country, page);
    }

    @Override
    public boolean existsBySupplierName(String name) {
        return supplierRepositoryPort.existsByName(name);
    }
    @Override
    public Optional<Partner> findSupplierExistsByEmail(String email) {
        return supplierRepositoryPort.findSupplierByEmail(email);
    }

    @Override
    @Transactional
    public void deleteSupplier(String id) {
      supplierRepositoryPort.deleteSupplierById(id);
    }


    @Override
    public Partner updateSupplier(String id, UpdatePartnerDTO partnerDTO) throws DataIntegrityViolationException{

        Partner updatedPartner = supplierRepositoryPort.findSupplierById(id)
                .orElseThrow(() -> BillingException.notFound("Fournisseur",id));

        PartnerMapper.updatePartnerFromDTO(partnerDTO,updatedPartner);

        return supplierRepositoryPort.updateSupplier(updatedPartner);
    }

    /************ CUSTOMER **********/

    @Override
    public Optional<Partner> createCustomer(PartnerForm partner) throws IOException {
        if(!Objects.equals(partner.getPartnerType(), PartnerType.CLIENT)){
            throw BillingException
                    .badRequest("Le Type est inadéquat");
        }
        if (customerRepositoryPort.existsByName(partner.getCompanyName())){
            throw BillingException
                    .alreadyExists(
                            "Client",
                            "Nom",
                            partner.getCompanyName());
        }
        if (this.customerExistsByRegistrationNumber(partner.getTaxRegistrationNumber())){
            throw BillingException
                    .alreadyExists(
                            "Client",
                            "Tax Registration Number",
                            partner.getTaxRegistrationNumber());
        }

        if (this.customerExistsByEmail(partner.getEmail())){
            throw BillingException
                    .alreadyExists(
                            "Client",
                            "Email",
                            partner.getEmail());
        }
        if (this.customerExistsByIban(partner.getIban())){
            throw BillingException
                    .alreadyExists(
                            "Client",
                            "Iban",
                            partner.getIban());
        }
        Partner  partnerModel = partnerMapper.createPartnerFromDTO(partner);
        Partner savedPartner = customerRepositoryPort.saveCustomer(partnerModel);
        return Optional.ofNullable(savedPartner);
                //customerRepositoryPort.findCustomerById(String.valueOf(savedPartner.getIdPartner()));
    }

    @Override
    public Page<PartnerItemDTO> getAllCustomers(String keyword , String Country ,int page) {
        return customerRepositoryPort.findAllCustomers(keyword, Country, page);
    }

    @Override
    public List<PartnerSummaryDTO> getSummaryClients(String keyword, String Country) {
        return customerRepositoryPort.getSummaryClients(keyword, Country);
    }

    @Override
    public Optional<Partner> findCustomerById(String id) {
        return customerRepositoryPort.findCustomerById(id);
    }

    @Override
    public boolean customerExistsByIdPartner(UUID idPartner) {
        return customerRepositoryPort.existsByIdPartner(idPartner);
    }

    @Override
    public boolean customerExistsByRegistrationNumber(String taxRegistrationNumber) {
        return customerRepositoryPort.existsByTaxRegistrationNumber(taxRegistrationNumber);
    }

    @Override
    public boolean customerExistsByEmail(String email) {
        return customerRepositoryPort.existsByEmail(email);
    }

    @Override
    public boolean customerExistsByIban(String iban) {
        return customerRepositoryPort.existsByIban(iban);
    }

    @Override
    @Transactional
    public void deleteCustomerById(String id) {
        customerRepositoryPort.deleteCustomerById(id);
    }

    @Override
    public Partner updateCustomer(String id, UpdatePartnerDTO partner) throws DataIntegrityViolationException{
       /* Partner updatedPartner = customerRepositoryPort.findCustomerById(id)
                .orElseThrow(() -> BillingException.notFound("Client",id));

        PartnerMapper.updatePartnerFromDTO(partner,updatedPartner);*/
        return  customerRepositoryPort.updateCustomer(id,partner);

    }


}
