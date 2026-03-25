package com.example.billingservice.infrastructure.out.persistance.validators;


import com.example.billingservice.infrastructure.out.persistance.dto.PartnerForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PartnerFormValidator implements ConstraintValidator<ValidPartnerForm, PartnerForm> {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 Mo

    private static final List<String> ALLOWED_TYPES = List.of(
            "application/pdf",
            "image/jpeg",
            "image/png"
    );
    @Override
    public boolean isValid(PartnerForm partnerForm, ConstraintValidatorContext constraintValidatorContext) {
        if (partnerForm == null) {
            return true;
        }

        boolean valid = true;
        constraintValidatorContext.disableDefaultConstraintViolation();

        valid &= validateFile(partnerForm.getRne(), "rne", constraintValidatorContext, true, "Le document RNE");
        valid &= validateFile(partnerForm.getPatente(), "patente", constraintValidatorContext, true, "Le document patente");
        valid &= validateFile(partnerForm.getContract(), "contract", constraintValidatorContext, true, "Le document contrat");

        return valid;
    }

    private boolean validateFile(MultipartFile file,
                                 String fieldName,
                                 ConstraintValidatorContext context,
                                 boolean required,
                                 String label) {

        if (file == null || file.isEmpty()) {
            if (required) {
                context.buildConstraintViolationWithTemplate(label + " est obligatoire")
                        .addPropertyNode(fieldName)
                        .addConstraintViolation();
                return false;
            }
            return true;
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            context.buildConstraintViolationWithTemplate(label + " doit être inférieur à 5 Mo")
                    .addPropertyNode(fieldName)
                    .addConstraintViolation();
            return false;
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            context.buildConstraintViolationWithTemplate(label + " doit être au format PDF, JPG ou PNG")
                    .addPropertyNode(fieldName)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
