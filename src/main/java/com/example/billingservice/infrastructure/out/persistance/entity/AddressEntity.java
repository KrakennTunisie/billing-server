package com.example.billingservice.infrastructure.out.persistance.entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identifiant unique", example = "550e8400-e29b-41d4-a716-446655440000")
    @Column(name = "id_address", updatable = false, nullable = false)
    private UUID idAddress;

    @Schema(description = "région", example = "Nabeul")
    @Column(name = "region")
    private String region;

    @Schema(description = "rue", example = "Rue de la fraternité")
    @Column(name = "street")
    private String street1;

    @Schema(description = "rue 2", example = "Rue de la fraternité")
    @Column(name = "street2")
    private String street2;

    @Schema(description = "ville", example = "Nabeul")
    @Column(name = "city")
    private String city;

    @Schema(description = "Etat", example = "Nabeul")
    @Column(name = "state")
    private String state;

    @Schema(description = "code postal", example = "8000")
    @Column(name = "zip_code")
    private String zipCode;

    @Schema(description = "Type", example = "Livraison")
    @Column(name = "address_type")
    private String addressType;
}