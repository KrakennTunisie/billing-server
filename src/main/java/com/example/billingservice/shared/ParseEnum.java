package com.example.billingservice.shared;

import com.example.billingservice.domain.exceptions.BillingException;

import java.util.Arrays;

public class ParseEnum {

    public static <T extends Enum<T>> T parseEnum(String value, Class<T> enumClass) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        BillingException.badRequest("Valeur invalide '" + value + "' pour " + enumClass.getSimpleName())
                );
    }
}
