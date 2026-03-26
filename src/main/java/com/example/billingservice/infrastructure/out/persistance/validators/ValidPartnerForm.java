package com.example.billingservice.infrastructure.out.persistance.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PartnerFormValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPartnerForm {
    String message() default "Le formulaire partenaire est invalide";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
