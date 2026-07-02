package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.domain.enums.PaymentCondition;
import com.example.billingservice.domain.model.Address;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.infrastructure.out.persistance.validators.ValidEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PartnerForm {


    private String active;

    private String enablePortal;

    private PartnerType partnerType;

    private String partnerName;

    private String maritalStatus;

    private String companyName;

    private String displayName;

    private String email;

    private String personnelPhoneNumber;

    private String professionnalPhoneNumber;

    private AddressDTO billingAddress;

    private AddressDTO shippingAddress;

    private String Language;

    private InvoiceCurrency currency;

    private String TaxRate;

    private String taxRegistrationNumber;

    private PaymentCondition paymentCondition;

    private String iban;


    private MultipartFile rne;


    private MultipartFile patente;


    private MultipartFile contract;
}
