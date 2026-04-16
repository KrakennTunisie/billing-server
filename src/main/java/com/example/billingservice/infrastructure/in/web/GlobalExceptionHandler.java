package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.exceptions.DatabaseException;
import com.example.billingservice.domain.exceptions.DocumentStorageException;
import com.example.billingservice.infrastructure.out.persistance.dto.ErrorResponse;
import com.example.billingservice.shared.DatabaseErrorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Case 1: @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return buildValidationErrors(ex.getBindingResult().getFieldErrors());
    }

    // Case 2: @ModelAttribute (Multipart)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, String>> handleBindException(BindException ex) {
        return buildValidationErrors(ex.getBindingResult().getFieldErrors());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSize(
            MaxUploadSizeExceededException ex
    ) {

        ErrorResponse error = new ErrorResponse(
                "FILE_TOO_LARGE",
                "Le fichier dépasse la taille maximale autorisée"
        );

        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(error);
    }

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex
    ) {
        DatabaseException dbException = new DatabaseException(
                "DATABASE_ERROR",
                DatabaseErrorUtils.getError(ex),
                ex
        );

        return ResponseEntity
                .status(dbException.getStatus())
                .body(new ErrorResponse(
                        dbException.getErrorCode(),
                        dbException.getMessage()
                ));
    }


    // fallback (important)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        System.out.println("Exception type: " + ex.getClass().getName());
        System.out.println("Message: " + ex.getMessage());

        ex.printStackTrace();

        ErrorResponse error = new ErrorResponse(
                "INTERNAL_ERROR",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }



    private ResponseEntity<Map<String, String>> buildValidationErrors(List<FieldError> fieldErrors) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
