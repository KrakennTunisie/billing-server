package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.model.Address;
import com.example.billingservice.infrastructure.out.persistance.dto.AddressDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.AddressEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper {

    // =========================
    // ENTITY → DOMAIN
    // =========================
    public Address toDomain(AddressEntity entity) {
        if (entity == null) {
            return null;
        }

        Address address = Address.builder()
                .idAddress(entity.getIdAddress())
                .addressType(entity.getAddressType())
                .region(entity.getRegion())
                .state(entity.getState())
                .city(entity.getCity())
                .street1(entity.getStreet1())
                .street2(entity.getStreet2())
                .zipCode(entity.getZipCode())
                .build();

        return address;
    }
    // =========================
    // DTO → domain
    // =========================
    public Address DTOtoDomain(AddressDTO dto) {
        if (dto == null) {
            return null;
        }

        Address address = Address.builder()
                .addressType(dto.getAddressType())
                .region(dto.getRegion())
                .state(dto.getState())
                .city(dto.getCity())
                .street1(dto.getStreet1())
                .street2(dto.getStreet2())
                .zipCode(dto.getZipCode())
                .build();

        return address;
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public AddressEntity toEntity(Address domain) {
        if (domain == null) {
            return null;
        }

        AddressEntity address = new AddressEntity();
        address.setIdAddress(domain.getIdAddress());
        address.setAddressType(domain.getAddressType());
        address.setCity(domain.getCity());
        address.setRegion(domain.getRegion());
        address.setState(domain.getState());
        address.setStreet1(domain.getStreet1());
        address.setStreet2(domain.getStreet2());
        address.setZipCode(domain.getZipCode());
        return address;
    }
    public AddressEntity updateToEntity(AddressEntity exsistingAddress ,Address updateAddress)
    {
        exsistingAddress.setAddressType(updateAddress.getAddressType());
        exsistingAddress.setCity(updateAddress.getCity());
        exsistingAddress.setRegion(updateAddress.getRegion());
        exsistingAddress.setState(updateAddress.getState());
        exsistingAddress.setStreet1(updateAddress.getStreet1());
        exsistingAddress.setStreet2(updateAddress.getStreet2());
        exsistingAddress.setZipCode(updateAddress.getZipCode());
        return exsistingAddress;
    }


}
