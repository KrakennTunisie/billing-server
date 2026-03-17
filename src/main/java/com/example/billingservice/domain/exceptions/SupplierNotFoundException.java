package com.example.billingservice.domain.exceptions;

import java.util.UUID;

public class SupplierNotFoundException extends RuntimeException {
    private final UUID customerId;

    public SupplierNotFoundException(UUID customerId) {
        super("Customer not found with id: " + customerId);
        this.customerId = customerId;
    }

    public UUID getCustomerId() {
        return customerId;
    }}
