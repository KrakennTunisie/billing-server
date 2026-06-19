package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.InvoiceStatsUseCase;
import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.application.service.GenerateInvoiceNumberService;
import com.example.billingservice.domain.enums.*;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
// ✅ Les méthodes HTTP (get, post, delete, patch, multipart)
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

// ✅ Les assertions (status, jsonPath)
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.ArgumentCaptor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reactor.retry.Repeat.times;


@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du InvoiceController")
class InvoiceControllerTest {
    @Mock
    private InvoiceUseCase invoiceUseCase;

    @Mock
    private InvoiceStatsUseCase invoiceStatsUseCase;

    @Mock
    private GenerateInvoiceNumberService generateInvoiceNumberService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private InvoiceController invoiceController;

    private MockMvc mockMvc;
    private static final UUID INVOICE_ID = UUID.randomUUID();
    private static final UUID CLIENT_ID  = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        invoiceController = new InvoiceController(
                invoiceUseCase,
                invoiceStatsUseCase,
                generateInvoiceNumberService,
                objectMapper
        );

        mockMvc = MockMvcBuilders
                .standaloneSetup(invoiceController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Doit retourner une facture client par ID")
    void shouldReturnClientInvoiceById() throws Exception {
        // ARRANGE
        InvoiceDTO dto = InvoiceDTO.builder()
                .idInvoice(INVOICE_ID)
                .invoiceNumber("FAC-2024-001")
                .invoiceStatus(InvoiceStatus.DRAFT)
                .build();

        when(invoiceUseCase.getClientInvoiceById(INVOICE_ID)).thenReturn(dto);

        // ACT & ASSERT
        mockMvc.perform(get("/api/invoices/client-invoices/{id}", INVOICE_ID))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.invoiceNumber").value("FAC-2024-001"))
                .andExpect(jsonPath("$.invoiceStatus").value("DRAFT"));
    }

    @Test
    @DisplayName("Doit créer une facture client")
    void shouldCreateClientInvoice() throws Exception {
        // ARRANGE — Items
        InvoiceItemCreateDTO item = InvoiceItemCreateDTO.builder()
                .description("Service de développement")
                .quantity(2)
                .unityPriceEXclTax(500.0)
                .vatRate(19.0)
                .itemTaxAmount(95.0)
                .itemTotalExclTax(1000.0)
                .itemTotalInclTax(1190.0)
                .operationCategory("SERVICE")
                .build();

        String invoiceItemsJson = objectMapper.writeValueAsString(List.of(item));

        // ARRANGE — Fichier multipart pour invoiceDocument
        MockMultipartFile invoiceDocument = new MockMultipartFile(
                "invoiceDocument",
                "invoice.pdf",
                "application/pdf",
                "fake-pdf-content".getBytes()
        );

        // ARRANGE — DTO de réponse
        InvoiceDTO responseDTO = InvoiceDTO.builder()
                .idInvoice(INVOICE_ID)
                .invoiceNumber("FAC-2024-001")
                .invoiceStatus(InvoiceStatus.DRAFT)
                .invoiceType(InvoiceType.SALE)
                .invoiceCurrency(InvoiceCurrency.EUR)
                .totalExclTaxEUR(1000.0)
                .totalInclTaxEUR(1190.0)
                .remainingAmount(1190.0)
                .vatRate(19.0)
                .paymentCondition(PaymentCondition.NET_30)
                .paymentMethod(PaymentMethod.BANK_TRANSFER)
                .appliedExchangeRate(3.2)
                .exchangeRateSource(ExchangeRateSource.CENTRAL_BANK)
                .hasInvoiceCreditNotes(false)
                .build();

        when(invoiceUseCase.createClientInvoice(any(InvoiceCreateDTO.class)))
                .thenReturn(responseDTO);

        // ACT & ASSERT
        mockMvc.perform(multipart("/api/invoices/client-invoices")
                        // Champ fichier
                        .file(invoiceDocument)
                        // Dates obligatoires
                        .param("issueDate", "2024-01-15")
                        .param("dueDate", "2024-02-15")
                        // Enums — utiliser la valeur exacte acceptée par @ValidEnum
                        .param("invoiceType", "SALE")
                        .param("exchangeRateSource", "CENTRAL_BANK")
                        // Autres champs obligatoires
                        .param("partner", CLIENT_ID.toString())
                        .param("invoiceNumber", "FAC-2024-001")
                        .param("invoiceCurrency", "EUR")
                        .param("paymentCondition", "NET_30")
                        .param("paymentMethod", "BANK_TRANSFER")
                        .param("vatRate", "19.0")
                        // Items JSON
                        .param("invoiceItemsList", invoiceItemsJson)
                        .contentType(org.springframework.http.MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idInvoice").value(INVOICE_ID.toString()))
                .andExpect(jsonPath("$.invoiceNumber").value("FAC-2024-001"))
                .andExpect(jsonPath("$.invoiceStatus").value("DRAFT"))
                .andExpect(jsonPath("$.invoiceType").value("SALE"))
                .andExpect(jsonPath("$.totalExclTaxEUR").value(1000.0))
                .andExpect(jsonPath("$.totalInclTaxEUR").value(1190.0))
                .andExpect(jsonPath("$.vatRate").value(19.0))
                .andExpect(jsonPath("$.exchangeRateSource").value("CENTRAL_BANK"));
    }


    @Test
    @DisplayName("Doit supprimer une facture client par ID")
    void shouldDeleteClientInvoiceById() throws Exception {
        // ARRANGE
        doNothing().when(invoiceUseCase).deleteClientInvoice(INVOICE_ID);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/invoices/client-invoices/{id}", INVOICE_ID))
                .andExpect(status().isNoContent());

        // Vérification que le use case a bien été appelé avec le bon ID
        verify(invoiceUseCase).deleteClientInvoice(INVOICE_ID);
    }

    @Test
    @DisplayName("Doit retourner 400 si l'ID n'est pas un UUID valide")
    void shouldReturn400WhenIdIsNotValidUUID() throws Exception {
        mockMvc.perform(delete("/api/invoices/client-invoices/{id}", "invalid-uuid"))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Doit mettre à jour le statut d'une facture fournisseur")
    void shouldUpdateSupplierInvoiceStatus() throws Exception {
        // ARRANGE
        // ARRANGE
        InvoiceDTO updatedInvoice = InvoiceDTO.builder()
                .idInvoice(INVOICE_ID)
                .invoiceStatus(InvoiceStatus.PAID)
                .build();

        when(invoiceUseCase.updateInvoiceStatus(INVOICE_ID, InvoiceStatus.PAID))
                .thenReturn(updatedInvoice);

        // ACT & ASSERT
        mockMvc.perform(patch("/api/invoices/supplier-invoices/{invoiceId}/status", INVOICE_ID)
                        .param("status", "PAID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idInvoice").value(INVOICE_ID.toString()))
                .andExpect(jsonPath("$.invoiceStatus").value("PAID"));

        // Vérification que le use case a bien été appelé avec les bons paramètres
        verify(invoiceUseCase).updateInvoiceStatus(INVOICE_ID, InvoiceStatus.PAID);
    }


    @Test
    @DisplayName("Doit mettre à jour une facture fournisseur")
    void shouldUpdateSupplierInvoice() throws Exception {
        // ARRANGE
        String invoiceItemsJson = """
        [
            {
                "description": "Service informatique",
                "quantity": 2,
                "unityPriceEXclTax": 500.0,
                "vatRate": 20.0,
                "itemTaxAmount": 200.0,
                "itemTotalExclTax": 1000.0,
                "itemTotalInclTax": 1200.0,
                "operationCategory": "SERVICE"
            }
        ]
        """;

        MockMultipartFile invoiceDocument = new MockMultipartFile(
                "invoiceDocument",
                "invoice.pdf",
                "application/pdf",
                "fake-pdf-content".getBytes()
        );

        when(invoiceUseCase.updateInvoice(any(InvoiceUpdateDTO.class)))
                .thenReturn(mock(InvoiceDTO.class));

        // ACT & ASSERT
        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/invoices/supplier-invoices")
                        .file(invoiceDocument)
                        .param("idInvoice", INVOICE_ID.toString())
                        .param("invoiceNumber", "FAC-2024-001")
                        .param("invoiceType", String.valueOf(InvoiceType.SALE))
                        .param("invoiceStatus", InvoiceStatus.TO_COLLECT.name())
                        .param("invoiceCurrency", InvoiceCurrency.EUR.name())
                        .param("paymentMethod", PaymentMethod.BANK_TRANSFER.name())
                        .param("paymentCondition", PaymentCondition.NET_30.name())
                        .param("exchangeRateSource", ExchangeRateSource.CENTRAL_BANK.name())
                        .param("vatRate", "20.0")
                        .param("issueDate", "2024-01-15")
                        .param("dueDate", "2024-02-15")
                        .param("invoiceItemsList", invoiceItemsJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

        verify(invoiceUseCase).updateInvoice(any(InvoiceUpdateDTO.class));
    }

    @Test
    @DisplayName("Doit retourner les statistiques des factures fournisseurs")
    void shouldGetSuppliersInvoicesStats() throws Exception {
        // ARRANGE
        ConvertedInvoiceStats stats = new ConvertedInvoiceStats(
                new BigDecimal("10000.00"),  // totalAmountTND
                new BigDecimal("3000.00"),   // totalAmountEUR
                new BigDecimal("3200.00"),   // totalAmountUSD

                10L,                         // totalInvoices
                6L,                          // paidInvoices
                4L,                          // pendingInvoices

                new BigDecimal("4000.00"),   // pendingAmountTND
                new BigDecimal("1200.00"),   // pendingAmountEUR
                new BigDecimal("1300.00"),   // pendingAmountUSD

                new BigDecimal("1000.00"),   // averageInvoiceTND
                new BigDecimal("300.00"),    // averageInvoiceEUR
                new BigDecimal("320.00")     // averageInvoiceUSD
        );

        when(invoiceStatsUseCase.getALLSupplierInvoiceStats()).thenReturn(stats);

        // ACT & ASSERT
        mockMvc.perform(get("/api/invoices/supplier-invoices/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmountTND").value(10000.00))
                .andExpect(jsonPath("$.totalAmountEUR").value(3000.00))
                .andExpect(jsonPath("$.totalAmountUSD").value(3200.00))
                .andExpect(jsonPath("$.totalInvoices").value(10))
                .andExpect(jsonPath("$.paidInvoices").value(6))
                .andExpect(jsonPath("$.pendingInvoices").value(4))
                .andExpect(jsonPath("$.pendingAmountTND").value(4000.00))
                .andExpect(jsonPath("$.pendingAmountEUR").value(1200.00))
                .andExpect(jsonPath("$.pendingAmountUSD").value(1300.00))
                .andExpect(jsonPath("$.averageInvoiceTND").value(1000.00))
                .andExpect(jsonPath("$.averageInvoiceEUR").value(300.00))
                .andExpect(jsonPath("$.averageInvoiceUSD").value(320.00));

        // Vérification que le use case a bien été appelé
        verify(invoiceStatsUseCase).getALLSupplierInvoiceStats();
    }
}