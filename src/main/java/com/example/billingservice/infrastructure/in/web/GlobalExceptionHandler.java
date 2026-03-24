package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.exceptions.DocumentStorageException;
import com.example.billingservice.infrastructure.out.persistance.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BillingException.class)
    public ResponseEntity<ErrorResponse> handleBillingException(BillingException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getErrorCode(),
                ex.getMessage()
        );

        return ResponseEntity
                .status(ex.getStatus())
                .body(error);
    }

    @ExceptionHandler(DocumentStorageException.class)
    public ResponseEntity<ErrorResponse> handleDocumentStorageException(DocumentStorageException ex) {
        ErrorResponse error = new ErrorResponse(
                "DOCUMENT_STORAGE_ERROR",
                ex.getMessage() + ex.getCause()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    // fallback (important)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

        ErrorResponse error = new ErrorResponse(
                "INTERNAL_ERROR",
                "Unexpected error occurred"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
