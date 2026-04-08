package com.example.billingservice.infrastructure.out.persistance.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class InvoiceCreditNoteItemCreateDTO {

    @NotNull(message = "L'id de la ligne de facture originale est obligatoire")
    private UUID originalInvoiceItemId;

    @NotNull(message = "La quantité à avoirer est obligatoire")
    @Min(value = 1, message = "La quantité à avoirer doit être supérieure à 0")
    private Integer quantity;
}
