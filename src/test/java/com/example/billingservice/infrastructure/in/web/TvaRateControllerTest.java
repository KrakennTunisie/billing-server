package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.TVARateUseCase;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseSettingCreateDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.BaseTVARateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class TvaRateControllerTest {
    @Mock
    private TVARateUseCase tvaRateUseCase;

    @InjectMocks
    private TvaRateController tvaRateController;

    private BaseSettingCreateDTO request;
    private BaseTVARateDTO expectedResponse;

    @BeforeEach
    void setUp() {
        request = BaseSettingCreateDTO.builder()
                .label("TVA Standard")
                .description("Taux de TVA standard à 19%")
                .build();

        expectedResponse = BaseTVARateDTO.builder()
                .label("TVA Standard")
                .description("Taux de TVA standard à 19%")
                .build();
        // ajustez selon les champs réels de BaseTVARateDTO (id, code, rate...)
    }

    @Test
    void create_shouldReturn201_whenRequestIsValid() {
        // Given
        when(tvaRateUseCase.create(any(BaseSettingCreateDTO.class)))
                .thenReturn(expectedResponse);

        // When
        ResponseEntity<BaseTVARateDTO> response = tvaRateController.create(request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getLabel()).isEqualTo("TVA Standard");

        verify(tvaRateUseCase, times(1)).create(request);
    }

    @Test
    void create_shouldPropagateException_whenUseCaseFails() {
        // Given
        when(tvaRateUseCase.create(any(BaseSettingCreateDTO.class)))
                .thenThrow(new RuntimeException("Erreur lors de la création du taux TVA"));

        // When / Then
        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> tvaRateController.create(request)
        );

        verify(tvaRateUseCase, times(1)).create(request);
    }
}
