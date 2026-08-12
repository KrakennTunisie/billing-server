package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.out.AuditEventPublisherPort;
import com.example.billingservice.application.ports.out.CustomerRepositoryPort;
import com.example.billingservice.application.ports.out.SupplierRepositoryPort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.infrastructure.out.messaging.AuditEvent;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.mapper.PartnerMapper;
import com.example.billingservice.shared.PartnerAuditEventFactory;
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
    private final AuditEventPublisherPort auditEventPublisherPort;
    private final PartnerAuditEventFactory partnerAuditEventFactory;


    /********* SUPPLIER ********/

    @Override
    public PartnerDetailsDTO createSupplier(PartnerForm partner) throws IOException, DataIntegrityViolationException {
        if(!Objects.equals(partner.getPartnerType(), PartnerType.SUPPLIER)){
            throw BillingException
                    .badRequest("Le Type est inadéquat");
        }
        if (supplierRepositoryPort.existsByCompanyName(partner.getCompanyName())){
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

        AuditEvent auditEvent = partnerAuditEventFactory.supplierCreated(
                partnerModel.getIdPartner(),
                String.valueOf(partnerModel.getIdPartner()),
                Map.of("partner", partnerModel.getCompanyName()),
                null
        );

        auditEventPublisherPort.publish(auditEvent);

        return partnerMapper.toDetailsDTO(supplierRepositoryPort.saveSupplier(partnerModel));
    }

    @Override
    public PartnerDetailsDTO addSupplierDocument(UUID idSupplier, MultipartFile document, DocumentType documentType) throws IOException, DataIntegrityViolationException {

        Partner partner = supplierRepositoryPort.findSupplierById(String.valueOf(idSupplier))
                .orElseThrow(() -> BillingException.notFound("Fournisseur", String.valueOf(idSupplier)));

        Document document1 = uploadDocument(partner.getTaxRegistrationNumber(), document, documentType);

        PartnerDetailsDTO partnerDetailsDTO =  supplierRepositoryPort.addDocumentToClient(idSupplier, document1);

        AuditEvent auditEvent = partnerAuditEventFactory.supplierDocumentAdded(
                partnerDetailsDTO.getIdPartner(),
                String.valueOf(partnerDetailsDTO.getIdPartner()),
                String.valueOf(document1.getIdDocument()),
                Map.of("add partner document", document1.getDocumentType().name()),
                null
        );

        auditEventPublisherPort.publish(auditEvent);

        return partnerDetailsDTO;

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
    public boolean supplierExistsByIbanAndIdPartnerNot(String iban, UUID idPartner) {
        return supplierRepositoryPort.existsByIbanAndIdPartnerNot(iban, idPartner);
    }

    @Override
    public boolean supplierExistsByEmailAndIdPartnerNot(String email, UUID idPartner) {
        return supplierRepositoryPort.existsByEmailAndIdPartnerNot(email, idPartner);
    }

    @Override
    public boolean supplierExistsByCompanyNameAndIdPartnerNot(String companyName, UUID idPartner) {
        return supplierRepositoryPort.existsByCompanyNameAndIdPartnerNot(companyName, idPartner);
    }

    @Override
    public boolean supplierExistsByTaxRegistrationNumberAndIdPartnerNot(String taxRegistrationNumber, UUID idPartner) {
        return supplierRepositoryPort.existsByTaxRegistrationNumberAndIdPartnerNot(taxRegistrationNumber, idPartner);
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

        if (supplierExistsByCompanyNameAndIdPartnerNot(partnerDTO.getCompanyName(), UUID.fromString(id))){
            throw BillingException.alreadyExists(
                    "Fournisseur",
                    "Nom de l'entreprise",
                    partnerDTO.getCompanyName());
        }
        if (!partnerDTO.getEmail().isBlank() &&
                supplierExistsByEmailAndIdPartnerNot(partnerDTO.getEmail(), UUID.fromString(id))){
            throw BillingException.alreadyExists(
                    "Fournisseur",
                    "email",
                    partnerDTO.getEmail());
        }
        if (!partnerDTO.getEmail().isBlank() &&
                supplierExistsByIbanAndIdPartnerNot(partnerDTO.getIban(), UUID.fromString(id))){
            throw BillingException.alreadyExists(
                    "Fournisseur",
                    "IBAN",
                    partnerDTO.getIban());
        }
        if (!partnerDTO.getEmail().isBlank() &&
                supplierExistsByTaxRegistrationNumberAndIdPartnerNot(partnerDTO.getTaxRegistrationNumber(), UUID.fromString(id))){
            throw BillingException.alreadyExists(
                    "Fournisseur",
                    "Matricule fiscal",
                    partnerDTO.getTaxRegistrationNumber());
        }

        PartnerMapper.updatePartnerFromDTO(partnerDTO,updatedPartner);

        Partner partner =  supplierRepositoryPort.updateSupplier(id,partnerDTO);


        AuditEvent auditEvent = partnerAuditEventFactory.supplierUpdated(
                partner.getIdPartner(),
                String.valueOf(partner.getIdPartner()),
                Map.of("partner", updatedPartner ),
                Map.of("partner", partner),
                null
        );

        auditEventPublisherPort.publish(auditEvent);

        return partner;
    }

    /************ CUSTOMER **********/

    @Override
    public PartnerDetailsDTO createCustomer(PartnerForm partner) throws IOException {
        if(!Objects.equals(partner.getPartnerType(), PartnerType.CLIENT)){
            throw BillingException
                    .badRequest("Le Type est inadéquat");
        }
        if (customerRepositoryPort.existsByCompanyName(partner.getCompanyName())){
            throw BillingException
                    .alreadyExists(
                            "Client",
                            "Nom de l'entreprise",
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

        AuditEvent auditEvent = partnerAuditEventFactory.clientCreated(
                partnerModel.getIdPartner(),
                String.valueOf(partnerModel.getIdPartner()),
                Map.of("partner", partnerModel.getCompanyName()),
                null
        );

        auditEventPublisherPort.publish(auditEvent);

        return partnerMapper.toDetailsDTO(savedPartner);


    }

    @Override
    public PartnerDetailsDTO addClientDocument(UUID idClient, MultipartFile document, DocumentType documentType) throws IOException, DataIntegrityViolationException {
        Partner partner = customerRepositoryPort.findCustomerById(String.valueOf(idClient))
                .orElseThrow(() -> BillingException.notFound("Client", String.valueOf(idClient)));

        Document document1 = uploadDocument(partner.getCompanyName(), document, documentType);

        PartnerDetailsDTO partnerDetailsDTO = customerRepositoryPort.addDocumentToClient(idClient, document1);

        AuditEvent auditEvent = partnerAuditEventFactory.clientDocumentAdded(
                partnerDetailsDTO.getIdPartner(),
                String.valueOf(partnerDetailsDTO.getIdPartner()),
                String.valueOf(document1.getIdDocument()),
                Map.of("add partner document", document1.getDocumentType().name()),
                null
        );

        auditEventPublisherPort.publish(auditEvent);

        return partnerDetailsDTO;
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
    public PartnerSummaryDTO getClientByCompanyName(String companyName) {
        return customerRepositoryPort.getCustomerByCompanyName(companyName);
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
    public boolean clientExistsByCompanyName(String companyName) {
        return customerRepositoryPort.existsByCompanyName(companyName);
    }

    @Override
    public boolean clientExistsByIbanAndIdPartnerNot(String iban, UUID idPartner) {
        return customerRepositoryPort.existsByIbanAndIdPartnerNot(iban, idPartner);
    }

    @Override
    public boolean clientExistsByEmailAndIdPartnerNot(String email, UUID idPartner) {
        return customerRepositoryPort.existsByEmailAndIdPartnerNot(email, idPartner);
    }

    @Override
    public boolean clientExistsByCompanyNameAndIdPartnerNot(String companyName, UUID idPartner) {
        return customerRepositoryPort.existsByCompanyNameAndIdPartnerNot(companyName, idPartner);
    }

    @Override
    public boolean clientExistsByTaxRegistrationNumberAndIdPartnerNot(String taxRegistrationNumber, UUID idPartner) {
        return customerRepositoryPort.existsByTaxRegistrationNumberAndIdPartnerNot(taxRegistrationNumber, idPartner);
    }

    @Override
    @Transactional
    public void deleteCustomerById(String id) {
        customerRepositoryPort.deleteCustomerById(id);
    }

    @Override
    public Partner updateCustomer(String id, UpdatePartnerDTO partner) throws DataIntegrityViolationException{

        if (clientExistsByCompanyNameAndIdPartnerNot(partner.getCompanyName(), UUID.fromString(id))){
            throw BillingException.alreadyExists(
                    "Client",
                    "Nom de l'entreprise",
                    partner.getCompanyName());
        }
        if (!partner.getEmail().isBlank() &&
                clientExistsByEmailAndIdPartnerNot(partner.getEmail(), UUID.fromString(id))){
            throw BillingException.alreadyExists(
                    "Client",
                    "email",
                    partner.getEmail());
        }
        if (!partner.getIban().isBlank() &&
                clientExistsByIbanAndIdPartnerNot(partner.getIban(), UUID.fromString(id))){
            throw BillingException.alreadyExists(
                    "Client",
                    "IBAN",
                    partner.getIban());
        }
        if (!partner.getTaxRegistrationNumber().isBlank() &&
                clientExistsByTaxRegistrationNumberAndIdPartnerNot(partner.getTaxRegistrationNumber(), UUID.fromString(id))){
            throw BillingException.alreadyExists(
                    "Client",
                    "Matricule fiscal",
                    partner.getTaxRegistrationNumber());
        }
        Partner partner1 =   customerRepositoryPort.updateCustomer(id,partner);


        AuditEvent auditEvent = partnerAuditEventFactory.supplierUpdated(
                partner1.getIdPartner(),
                String.valueOf(partner1.getIdPartner()),
                Map.of("partner", partner ),
                Map.of("partner", partner1),
                null
        );

        auditEventPublisherPort.publish(auditEvent);

        return partner1;
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
