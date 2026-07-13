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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PartnerService implements PartnerUseCase  {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final SupplierRepositoryPort supplierRepositoryPort;
    private final PartnerMapper partnerMapper;
    private final UploadDocumentService uploadDocumentService;


    /********* SUPPLIER ********/

    @Override
    public PartnerDetailsDTO createSupplier(PartnerForm partner) throws IOException, DataIntegrityViolationException {
        if(!Objects.equals(partner.getPartnerType(), PartnerType.SUPPLIER)){
            throw BillingException
                    .badRequest("Le Type est inadéquat");
        }
        if (supplierRepositoryPort.existsByName(partner.getCompanyName())){
            throw BillingException
                    .alreadyExists(
                            "Fournisseur",
                            "Nom",
                            partner.getCompanyName());
        }
        /*if (supplierRepositoryPort.existsByTaxRegistrationNumber(partner.getTaxRegistrationNumber())){
            throw BillingException
                    .alreadyExists(
                            "Fournisseur",
                            "Tax Registration Number",
                            partner.getTaxRegistrationNumber());
        }
        if (customerRepositoryPort.existsByEmail(partner.getEmail())){
            throw BillingException
                    .alreadyExists(
                            "Fournisseur",
                            "Email",
                            partner.getEmail());
        }
        if (supplierRepositoryPort.existsByIban(partner.getIban())){
            throw BillingException
                    .alreadyExists(
                            "Fournisseur",
                            "Iban",
                            partner.getIban());
        }*/
        Partner  partnerModel =partnerMapper.createPartnerFromDTO(partner);
        return partnerMapper.toDetailsDTO(supplierRepositoryPort.saveSupplier(partnerModel));
    }

    @Override
    public PartnerDetailsDTO addSupplierDocument(UUID idSupplier, MultipartFile document, DocumentType documentType) throws IOException, DataIntegrityViolationException {

        Partner partner = supplierRepositoryPort.findSupplierById(String.valueOf(idSupplier))
                .orElseThrow(() -> BillingException.notFound("Fournisseur", String.valueOf(idSupplier)));

        Document document1 = uploadDocument(partner.getTaxRegistrationNumber(), document, documentType);

        return supplierRepositoryPort.addDocumentToClient(idSupplier, document1);

    }


    @Override
    public Optional<Partner> getSupplierById(String id) {
        return supplierRepositoryPort.findSupplierById(id);
    }

    @Override
    public PartnerDetailsDTO getSupplierDetailsById(String idSupplier) {
        if(!supplierRepositoryPort.existsByIdPartner(UUID.fromString(idSupplier))){
            throw BillingException.notFound("Fournisseur", idSupplier);
        }
        return supplierRepositoryPort.getSupplierDetailsById(UUID.fromString(idSupplier));
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
    public boolean supplierExistsByCompanyName(String companyName) {
        return supplierRepositoryPort.existsByCompanyName(companyName);
    }

    @Override
    public PartnerSummaryDTO getSupplierByCompanyName(String companyName) {
        return supplierRepositoryPort.getSupplierByCompanyName(companyName);
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
    public void updateSupplierStatus(String idSupplier, Boolean status) {
        supplierRepositoryPort.updateSupplierStatus(idSupplier,status);
    }

    @Override
    public List<PartnerSummaryDTO> getSummarySuppliers(String keyword, String Country) {
        return supplierRepositoryPort.getSummarySuppliers(keyword, Country);
    }


    @Override
    public Partner updateSupplier(String id, UpdatePartnerDTO partnerDTO) throws DataIntegrityViolationException{

        Partner updatedPartner = supplierRepositoryPort.findSupplierById(id)
                .orElseThrow(() -> BillingException.notFound("Fournisseur",id));

        PartnerMapper.updatePartnerFromDTO(partnerDTO,updatedPartner);

        return supplierRepositoryPort.updateSupplier(id,partnerDTO);
    }

    /************ CUSTOMER **********/

    @Override
    public PartnerDetailsDTO createCustomer(PartnerForm partner) throws IOException {
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
       /* if (customerRepositoryPort.existsByTaxRegistrationNumber(partner.getTaxRegistrationNumber())){
            throw BillingException
                    .alreadyExists(
                            "Client",
                            "Tax Registration Number",
                            partner.getTaxRegistrationNumber());
        }

        if (customerRepositoryPort.existsByEmail(partner.getEmail())){
            throw BillingException
                    .alreadyExists(
                            "Client",
                            "Email",
                            partner.getEmail());
        }
        if (customerRepositoryPort.existsByIban(partner.getIban())){
            throw BillingException
                    .alreadyExists(
                            "Client",
                            "Iban",
                            partner.getIban());
        }*/
        Partner  partnerModel = partnerMapper.createPartnerFromDTO(partner);
        Partner savedPartner = customerRepositoryPort.saveCustomer(partnerModel);
        return partnerMapper.toDetailsDTO(savedPartner);
    }

    @Override
    public PartnerDetailsDTO addClientDocument(UUID idClient, MultipartFile document, DocumentType documentType) throws IOException, DataIntegrityViolationException {
        Partner partner = customerRepositoryPort.findCustomerById(String.valueOf(idClient))
                .orElseThrow(() -> BillingException.notFound("Client", String.valueOf(idClient)));

        Document document1 = uploadDocument(partner.getCompanyName(), document, documentType);

        return customerRepositoryPort.addDocumentToClient(idClient, document1);
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
    public Optional<Partner> findCustomerByEmail(String email) {
        return customerRepositoryPort.findCustomerByEmail(email);
    }

    @Override
    public PartnerDetailsDTO getClientDetailsById(String idClient) {
        if(!customerRepositoryPort.existsByIdPartner(UUID.fromString(idClient))){
            throw BillingException.notFound("Client", idClient);
        }
        return customerRepositoryPort.getClientDetailsById(UUID.fromString(idClient));
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
        return  customerRepositoryPort.updateCustomer(id,partner);

    }

    @Override
    public void updateCustomerStatus(String idClient, Boolean status) {
        customerRepositoryPort.updateCustomerStatus(idClient,status);
    }


    private Document uploadDocument(String companyName, MultipartFile multipartFile, DocumentType documentType) throws IOException {
        if (multipartFile == null || companyName == null || documentType == null) {
            return null;
        }

        UploadedFile uploadedFile = new UploadedFile(
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                multipartFile.getInputStream(),
                multipartFile.getSize()
        );

        Document uploadedDocument = uploadDocumentService.upload(
                companyName,
                documentType,
                uploadedFile
        );

        return uploadedDocument;
    }


}
