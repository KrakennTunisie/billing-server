package com.example.billingservice.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BillingException extends RuntimeException{

    private final HttpStatus status;
    private final String errorCode;

    public BillingException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }


    public static BillingException notFound(String resource, String id) {
        return new BillingException(
                HttpStatus.NOT_FOUND,
                "NOT_FOUND",
                resource + " not found with id: " + id
        );
    }

    public static BillingException alreadyExists(String resource, String field, String value) {
        return new BillingException(
                HttpStatus.CONFLICT,
                "ALREADY_EXISTS",
                resource + " already exists with " + field + ": " + value
        );
    }

    public static BillingException badRequest(String message) {
        return new BillingException(
                HttpStatus.BAD_REQUEST,
                "BAD_REQUEST",
                message
        );
    }

    public static BillingException forbidden(String message) {
        return new BillingException(
                HttpStatus.FORBIDDEN,
                "FORBIDDEN",
                message
        );
    }

    public static BillingException internalError(String message) {
        return new BillingException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                message
        );
    }
}
