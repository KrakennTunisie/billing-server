package com.example.billingservice.domain.exceptions;

public class SupplierNotFoundException extends RuntimeException {
  public SupplierNotFoundException(String message) {
    super(message);
  }
}
