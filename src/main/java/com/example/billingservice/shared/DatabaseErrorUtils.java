package com.example.billingservice.shared;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DatabaseErrorUtils {

    public static String getError(Throwable message){
        String rawMessage = extractDeepestMessage(message);
        return buildDatabaseMessage(rawMessage);
    }

    private static String extractDeepestMessage(Throwable ex) {
        Throwable current = ex;
        String message = ex.getMessage();

        while (current != null) {
            if (current.getMessage() != null && !current.getMessage().isBlank()) {
                message = current.getMessage();
            }
            current = current.getCause();
        }

        return message != null ? message : "";
    }
    public record FieldValue(String field, String value) {}

    private static FieldValue extractFieldAndValue(String message) {
        if (message == null) return new FieldValue("unknown", "unknown");

        Pattern pattern = Pattern.compile("\\((.+?)\\)=\\((.+?)\\)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return new FieldValue(matcher.group(1), matcher.group(2));
        }

        return new FieldValue("unknown", "unknown");
    }

    private static String extractField(String message) {
        if (message == null) return "unknown";

        // Postgres NOT NULL: column "email" violates not-null constraint
        Pattern pattern = Pattern.compile("column \"(.+?)\"");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "unknown";
    }

    private static String mapFieldLabel(String field) {
        return switch (field.toLowerCase()) {
            case "email" -> "Email";
            case "tax_registration_number" -> "Tax registration number";
            default -> field;
        };
    }

    private static String buildDatabaseMessage(String message) {

        // UNIQUE
        if (message.contains("duplicate")
                || message.contains("contrainte unique")
                || message.contains("UNIQUE constraint")) {

            FieldValue fv = extractFieldAndValue(message);
            return mapFieldLabel(fv.field()) + " dèjà existant: " + fv.value();
        }

        //  NOT NULL
        if (message.contains("not-null")
                || message.contains("null value")
                || message.contains("violates not-null")) {

            String field = extractField(message);
            return mapFieldLabel(field) + " is required";
        }

        //  FOREIGN KEY
        if (message.contains("foreign key")
                || message.contains("violates foreign key")) {

            return "Invalid reference";
        }

        return "Database error";
    }
}
