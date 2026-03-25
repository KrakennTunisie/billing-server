package com.example.billingservice.domain.exceptions;

import org.springframework.http.HttpStatus;


public class DatabaseException extends BillingException {

    public DatabaseException(String errorCode, String message, Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, errorCode, message, cause);
    }


}