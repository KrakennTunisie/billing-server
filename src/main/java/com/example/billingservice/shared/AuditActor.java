package com.example.billingservice.shared;

import java.util.List;

public record AuditActor(
        String userId,
        String firstName,
        String lastName,
        List<String> roles
) {
    @Override
    public String toString() {
        return "AuditActor{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roles=" + roles +
                '}';
    }
}

