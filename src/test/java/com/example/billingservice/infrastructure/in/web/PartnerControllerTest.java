package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.enums.PartnerType;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerDetailsDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PartnerForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PartnerControllerTest {
    @Mock
    private PartnerUseCase partnerUseCase;

    @InjectMocks
    private PartnerController partnerController;

    private PartnerForm form;
    private PartnerDetailsDTO expectedResponse;

    @BeforeEach
    void setUp() {
        form = new PartnerForm(
                "true",                 // active
                "true",                 // enablePortal
                PartnerType.SUPPLIER,   // partnerType
                "John Doe",             // partnerName
                "SINGLE",               // maritalStatus
                "ACME Corp",            // companyName
                "ACME",                 // displayName
                "contact@acme.com",     // email
                "12345678",             // personnelPhoneNumber
                "87654321",             // professionnalPhoneNumber
                null,                   // billingAddress
                null,                   // shippingAddress
                "FR",                   // Language
                InvoiceCurrency.EUR,    // currency
                "19",                   // TaxRate
                "TAX123",               // taxRegistrationNumber
                "30_DAYS",              // paymentCondition
                "FR7630006000011234567890189", // iban
                new MockMultipartFile("rne", "rne.pdf", "application/pdf", "contenu".getBytes()),
                new MockMultipartFile("patente", "patente.pdf", "application/pdf", "contenu".getBytes()),
                new MockMultipartFile("contract", "contract.pdf", "application/pdf", "contenu".getBytes())
        );

        expectedResponse = PartnerDetailsDTO.builder()
                .idPartner(UUID.randomUUID())
                .active(true)
                .enablePortal(true)
                .partnerName("John Doe")
                .companyName("ACME Corp")
                .displayName("ACME")
                .email("contact@acme.com")
                .partnerType(PartnerType.SUPPLIER)
                .currency(InvoiceCurrency.EUR)
                .build();
    }

    @Test
    void devraitCreerUnFournisseurAvecSucces() throws IOException {
        // given
        when(partnerUseCase.createSupplier(form)).thenReturn(expectedResponse);

        // when
        ResponseEntity<PartnerDetailsDTO> response = partnerController.createSupplier(form);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(partnerUseCase).createSupplier(form);
    }

    @Test
    void devraitPropagerIOExceptionLorsDeLUpload() throws IOException {
        // given
        when(partnerUseCase.createSupplier(any())).thenThrow(new IOException("erreur upload fichier"));

        // when / then
        assertThrows(IOException.class, () -> partnerController.createSupplier(form));
        verify(partnerUseCase).createSupplier(form);
    }

    @Test
    void devraitPropagerDataIntegrityViolationException() throws IOException {
        // given
        when(partnerUseCase.createSupplier(any()))
                .thenThrow(new DataIntegrityViolationException("doublon fournisseur"));

        // when / then
        assertThrows(DataIntegrityViolationException.class,
                () -> partnerController.createSupplier(form));
    }
}
