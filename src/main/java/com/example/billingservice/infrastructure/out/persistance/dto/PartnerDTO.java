package com.example.billingservice.infrastructure.out.persistance.dto;


import com.example.billingservice.domain.enums.PartnerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@Schema(description = "Données du partenaire")
public class PartnerDTO {
    @Schema(description = "Nom du partenaire", example = "Oumaima chelly")
    private String name;
    @Schema(description = "Email du partenaire", example = "oumaima@example.com")
    private String email;
    @Schema(description = "Numéro de téléphone", example = "+21621495185")
    private String phoneNumber;
    @Schema(description = "Numéro Tax", example = "Tax12585")
    private String taxRegistrationNumber;
    @Schema(description = "Pays", example = "Tunisie")
    private String country;
    @Schema(description = "Addrese", example = "Tunis, Lac2 , résidence el-wafa")
    private String address;
    @Schema(description = "IBAN", example = "5454d5ds53")
    private String iban;
    @Schema(description = "Type de partenaire", example = "SUPPLIER")
    private PartnerType partnerType;

    @Schema(description = "Document RNE")
    private MultipartFile rne;

    @Schema(description = "Document Patente")
    private MultipartFile patente;

    @Schema(description = "Document Contrat")
    private MultipartFile contract;
}
