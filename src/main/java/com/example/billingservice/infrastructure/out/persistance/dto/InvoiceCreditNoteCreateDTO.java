package com.example.billingservice.infrastructure.out.persistance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class InvoiceCreditNoteCreateDTO {
    @NotBlank(message = "L'id de la facture originale est obligatoire")
    private String originalInvoiceId;

    @Setter
    private String invoiceCreditNoteNumber;

    @NotBlank(message = "Le motif est obligatoire")
    private String motif;

    @NotBlank(message = "La description est obligatoire")
    private String description;


    @NotNull(message = "Le document de facture est obligatoire")
    private MultipartFile invoiceDocument;

   // @NotEmpty(message = "La liste des lignes sélectionnées est obligatoire")
   // @Valid
    private List<InvoiceCreditNoteItemCreateDTO> invoiceItems;
}
