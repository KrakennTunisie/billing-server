package com.example.billingservice.domain.exceptions;

public class DocumentStorageException extends RuntimeException {
  public DocumentStorageException(String message) {
    super(message);
  }
}
