package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.infrastructure.out.persistance.validators.ValidEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class PartnerForm {

    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email est invalide")
    private String email;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String phoneNumber;

    @NotBlank(message = "Le matricule fiscal est obligatoire")
    private String taxRegistrationNumber;

    @NotBlank(message = "Le pays est obligatoire")
    private String country;

    @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    @NotBlank(message = "L'IBAN est obligatoire")
    private String iban;

    @NotNull(message = "Le type de partenaire est obligatoire")
    @ValidEnum(enumClass = PartnerType.class, message = "Type de partenaire invalide")
    private String partnerType;

    @NotNull(message = "Le document rne est obligatoire")
    private MultipartFile rne;

    @NotNull(message = "Le document patente est obligatoire")
    private MultipartFile patente;

    @NotNull(message = "Le document contract est obligatoire")
    private MultipartFile contract;
}
