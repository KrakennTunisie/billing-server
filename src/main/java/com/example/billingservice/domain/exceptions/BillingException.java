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
    public BillingException(HttpStatus status, String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }


    public static BillingException notFound(String resource, String id) {
        return new BillingException(
                HttpStatus.NOT_FOUND,
                "NOT_FOUND",
                resource + " avec id: " + id+ " est introuvable !"
        );
    }

    public static BillingException alreadyExists(String resource, String field, String value) {
        return new BillingException(
                HttpStatus.CONFLICT,
                "ALREADY_EXISTS",
                resource + " Dèjà existant avec " + field + ": " + value
        );
    }

    public static BillingException badRequest(String message) {
        return new BillingException(
                HttpStatus.BAD_REQUEST,
                "BAD_REQUEST",
                message
        );
    }

    public static BillingException fileTooLarge(String message) {
        return new BillingException(
                HttpStatus.BAD_REQUEST,
                "FILE_TOO_LARGE",
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
