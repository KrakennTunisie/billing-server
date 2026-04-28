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



    /********* SUPPLIER ********/

    @Override
    public Partner createSupplier(PartnerForm partner) throws IOException, DataIntegrityViolationException {
        if(!Objects.equals(partner.getPartnerType(), PartnerType.SUPPLIER.name())){
            throw BillingException
                    .badRequest("Le Type est inadéquat");
        }
        if (this.supplierRepositoryPort.existsByName(partner.getName())){
            throw BillingException
                    .alreadyExists(
                            "Fournisseur",
                            "Nom",
                            partner.getName());
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



        UploadedFile rne = new UploadedFile(
                partner.getRne().getOriginalFilename(),
                partner.getRne().getContentType(),
                partner.getRne().getInputStream(),
                partner.getRne().getSize()
        );

        UploadedFile contract = new UploadedFile(
                partner.getContract().getOriginalFilename(),
                partner.getContract().getContentType(),
                partner.getContract().getInputStream(),
                partner.getContract().getSize()
        );

        UploadedFile patente = new UploadedFile(
                partner.getPatente().getOriginalFilename(),
                partner.getPatente().getContentType(),
                partner.getPatente().getInputStream(),
                partner.getPatente().getSize()
        );

         Document uploadedRne = uploadDocumentService.upload(partner.getTaxRegistrationNumber(), DocumentType.RNE, rne);
         Document uploadedContract = uploadDocumentService.upload(partner.getTaxRegistrationNumber(), DocumentType.CONTRACT, contract);
         Document uploadedPatente = uploadDocumentService.upload(partner.getTaxRegistrationNumber(), DocumentType.PATENT, patente);

         Partner  partnerModel = Partner.builder().name(partner.getName()).email(partner.getEmail()).phoneNumber(partner.getPhoneNumber())
                .taxRegistrationNumber(partner.getTaxRegistrationNumber()).country(partner.getCountry())
                .address(partner.getAddress()).iban(partner.getIban()).partnerType(PartnerType.valueOf(partner.getPartnerType()))
                .rne(uploadedRne).contract(uploadedContract).patente(uploadedPatente).build();

        return supplierRepositoryPort.saveSupplier(partnerModel);
    }

    @Override
    public Optional<Partner> getSupplierById(String id) {
        return supplierRepositoryPort.findSupplierById(id);
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
    public Page<PartnerItemDTO> getAllSuppliers(String keyword , String Country , int page) {
        return  supplierRepositoryPort
                .findAllSuppliers(keyword, Country, page);
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
        if(!Objects.equals(partner.getPartnerType(), PartnerType.CLIENT.name())){
            throw BillingException
                    .badRequest("Le Type est inadéquat");
        }
        if (customerRepositoryPort.existsByName(partner.getName())){
            throw BillingException
                    .alreadyExists(
                            "Client",
                            "Nom",
                            partner.getName());
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
        UploadedFile rne = new UploadedFile(
                partner.getRne().getOriginalFilename(),
                partner.getRne().getContentType(),
                partner.getRne().getInputStream(),
                partner.getPatente().getSize()
        );

        UploadedFile contract = new UploadedFile(
                partner.getContract().getOriginalFilename(),
                partner.getContract().getContentType(),
                partner.getContract().getInputStream(),
                partner.getContract().getSize()
        );

        UploadedFile patente = new UploadedFile(
                partner.getPatente().getOriginalFilename(),
                partner.getPatente().getContentType(),
                partner.getPatente().getInputStream(),
                partner.getPatente().getSize()
        );

        Document uploadedRne = uploadDocumentService.upload(partner.getTaxRegistrationNumber(), DocumentType.RNE, rne);
        Document uploadedContract = uploadDocumentService.upload(partner.getTaxRegistrationNumber(), DocumentType.CONTRACT, contract);
        Document uploadedPatente = uploadDocumentService.upload(partner.getTaxRegistrationNumber(), DocumentType.PATENT, patente);

        Partner  partnerModel = Partner.builder().name(partner.getName()).email(partner.getEmail()).phoneNumber(partner.getPhoneNumber())
                .taxRegistrationNumber(partner.getTaxRegistrationNumber()).country(partner.getCountry())
                .address(partner.getAddress()).iban(partner.getIban()).partnerType(PartnerType.valueOf(partner.getPartnerType()))
                .rne(uploadedRne).contract(uploadedContract).patente(uploadedPatente).build();

        Partner savedPartner = customerRepositoryPort.saveCustomer(partnerModel);

        return customerRepositoryPort.findCustomerById(String.valueOf(savedPartner.getIdPartner()));
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
        Partner updatedPartner = customerRepositoryPort.findCustomerById(id)
                .orElseThrow(() -> BillingException.notFound("Client",id));

        PartnerMapper.updatePartnerFromDTO(partner,updatedPartner);
        return  customerRepositoryPort.updateCustomer(updatedPartner);

    }


}
