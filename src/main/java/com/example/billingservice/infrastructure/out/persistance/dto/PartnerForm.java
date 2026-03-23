package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.PartnerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class PartnerForm {
    private String name;
    private String email;
    private String phoneNumber;
    private String taxRegistrationNumber;
    private String country;
    private String address;
    private String iban;
    private PartnerType partnerType;

    private MultipartFile rne;

    private MultipartFile patente;

    private MultipartFile contract;
}
