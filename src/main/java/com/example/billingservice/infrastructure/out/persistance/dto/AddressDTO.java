package com.example.billingservice.infrastructure.out.persistance.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Builder
public class AddressDTO {

        private String region;

        private String state;

        private String street1;

        private String street2;

        private String city;

        private String zipCode;

        private String addressType;

}
