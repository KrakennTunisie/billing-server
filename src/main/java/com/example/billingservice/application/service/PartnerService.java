package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.out.CustomerRepositoryPort;
import com.example.billingservice.application.ports.out.SupplierRepositoryPort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerDTO;
import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.domain.model.Partner;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerForm;
import com.example.billingservice.infrastructure.out.persistance.dto.UploadedFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartnerService implements PartnerUseCase  {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final SupplierRepositoryPort supplierRepositoryPort;
    private final UploadCustomerDocumentService uploadCustomerDocumentService;
    private final UploadSupplierDocumentService uploadSupplierDocumentService;



    /********* SUPPLIER ********/

    @Override
    public Optional<Partner> createSupplier(PartnerForm partner) throws IOException {



        UploadedFile rne = new UploadedFile(
                partner.getRne().getOriginalFilename(),
                partner.getRne().getContentType(),
                partner.getRne().getBytes()
        );

        UploadedFile contract = new UploadedFile(
                partner.getContract().getOriginalFilename(),
                partner.getContract().getContentType(),
                partner.getContract().getBytes()
        );

        UploadedFile patente = new UploadedFile(
                partner.getPatente().getOriginalFilename(),
                partner.getPatente().getContentType(),
                partner.getPatente().getBytes()
        );


         Document uploadedRne = uploadSupplierDocumentService.upload(partner.getTaxRegistrationNumber(), DocumentType.RNE, rne);
         Document uploadedContract = uploadSupplierDocumentService.upload(partner.getTaxRegistrationNumber(), DocumentType.CONTRACT, contract);
         Document uploadedPatente =uploadSupplierDocumentService.upload(partner.getTaxRegistrationNumber(), DocumentType.PATENT, patente);

         Partner  partnerModel = Partner.builder().name(partner.getName()).email(partner.getEmail()).phoneNumber(partner.getPhoneNumber())
                .taxRegistrationNumber(partner.getTaxRegistrationNumber()).country(partner.getCountry())
                .address(partner.getAddress()).iban(partner.getIban()).partnerType(partner.getPartnerType())
                .rne(uploadedRne).contract(uploadedContract).patente(uploadedPatente).build();

         Partner savedPartner = supplierRepositoryPort.saveSupplier(partnerModel);

         return supplierRepositoryPort.findSupplierById(String.valueOf(savedPartner.getIdPartner()));
    }

    @Override
    public Optional<Partner> getSupplierById(String id) {
        return supplierRepositoryPort.findSupplierById(id);
    }

    @Override
    public Page<Partner> getAllSuppliers(String keyword , String Country ,int page) {
        return  supplierRepositoryPort.findAllSuppliers(keyword, Country, page);
    }

    @Override
    public void deleteSupplier(String id) {
      supplierRepositoryPort.deleteSupplierById(id);
    }


    @Override
    public Partner updateSupplier(String id, PartnerDTO partnerDTO) {

        Optional <Partner> existing = supplierRepositoryPort.findSupplierById(id);

        if (existing.isPresent()) {
            Partner updated = Partner.builder()
                    .idPartner(existing.get().getIdPartner())
                    .name(partnerDTO.getName())
                    .email(partnerDTO.getEmail())
                    .phoneNumber(partnerDTO.getPhoneNumber())
                    .country(partnerDTO.getCountry())
                    .address(partnerDTO.getAddress())
                    .iban(partnerDTO.getIban())
                    .partnerType(existing.get().getPartnerType())
                    .taxRegistrationNumber(existing.get().getTaxRegistrationNumber())
                    .rne(existing.get().getRne())
                    .patente(existing.get().getPatente())
                    .contract(existing.get().getContract())
                    .build();
            return supplierRepositoryPort.updateSupplier(updated);
        }
        else {
            throw BillingException.notFound("Supplier not found ",id);
        }
    }

    /************ CUSTOMER **********/

    @Override
    public Optional<Partner> createCustomer(PartnerForm partner) throws IOException {
        UploadedFile rne = new UploadedFile(
                partner.getRne().getOriginalFilename(),
                partner.getRne().getContentType(),
                partner.getRne().getBytes()
        );

        UploadedFile contract = new UploadedFile(
                partner.getContract().getOriginalFilename(),
                partner.getContract().getContentType(),
                partner.getContract().getBytes()
        );

        UploadedFile patente = new UploadedFile(
                partner.getPatente().getOriginalFilename(),
                partner.getPatente().getContentType(),
                partner.getPatente().getBytes()
        );

        Document uploadedRne = uploadSupplierDocumentService.upload(partner.getTaxRegistrationNumber(), DocumentType.RNE, rne);
        Document uploadedContract = uploadSupplierDocumentService.upload(partner.getTaxRegistrationNumber(), DocumentType.CONTRACT, contract);
        Document uploadedPatente =uploadSupplierDocumentService.upload(partner.getTaxRegistrationNumber(), DocumentType.PATENT, patente);

        Partner  partnerModel = Partner.builder().name(partner.getName()).email(partner.getEmail()).phoneNumber(partner.getPhoneNumber())
                .taxRegistrationNumber(partner.getTaxRegistrationNumber()).country(partner.getCountry())
                .address(partner.getAddress()).iban(partner.getIban()).partnerType(partner.getPartnerType())
                .rne(uploadedRne).contract(uploadedContract).patente(uploadedPatente).build();

        Partner savedPartner = customerRepositoryPort.saveCustomer(partnerModel);

        return customerRepositoryPort.findCustomerById(String.valueOf(savedPartner.getIdPartner()));
    }

    @Override
    public Page<Partner> getAllCustomers(String keyword , String Country ,int page) {
        return customerRepositoryPort.findAllCustomers(keyword, Country, page);
    }

    @Override
    public Optional<Partner> findCustomerById(String id) {
        return customerRepositoryPort.findCustomerById(id);
    }

    @Override
    public void deleteCustomerById(String id) {
        customerRepositoryPort.deleteCustomerById(id);
    }

    @Override
    public Partner updateCustomer(String id, PartnerDTO partner) {
        Optional <Partner> existing = customerRepositoryPort.findCustomerById(id);

        if (existing.isPresent()) {
            Partner updated = Partner.builder()
                    .idPartner(existing.get().getIdPartner())
                    .name(partner.getName())
                    .email(partner.getEmail())
                    .phoneNumber(partner.getPhoneNumber())
                    .country(partner.getCountry())
                    .address(partner.getAddress())
                    .iban(partner.getIban())
                    .partnerType(existing.get().getPartnerType())
                    .taxRegistrationNumber(existing.get().getTaxRegistrationNumber())
                    .rne(existing.get().getRne())
                    .patente(existing.get().getPatente())
                    .contract(existing.get().getContract())
                    .build();
            return customerRepositoryPort.updateCustomer(updated);
        }
        else {
            throw BillingException.notFound("Customer not found ",id);
        }
    }




}
