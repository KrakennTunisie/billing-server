package com.example.billingservice.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Modèle addresse")
public class Address {
    @Schema(description = "Identifiant unique", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID idAddress;
    @Schema(description = "région", example = "Nabeul")
    private String region;
    @Schema(description = "state", example = "Nabeul")
    private String state;
    @Schema(description = "rue", example = "Rue de la fraternité")
    private String street1;
    @Schema(description = "rue", example = "Rue de la fraternité")
    private String street2;
    @Schema(description = "ville", example = "Nabeul")
    private String city;
    @Schema(description = "code postal", example = "8000")
    private String zipCode;
    @Schema(description = "Type", example = "Livraison")
    private String addressType;

}
