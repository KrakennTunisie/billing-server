package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.InvoiceCreditNoteStatus;
import com.example.billingservice.infrastructure.out.persistance.validators.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInvoiceCreditNoteStatusRequest {

    @NotNull(message = "Invoice status is required")
    @NotBlank(message = "Le statut est obligatoire")
    @ValidEnum(enumClass = InvoiceCreditNoteStatus.class, message = "Status invalid")
    private String status;
}
