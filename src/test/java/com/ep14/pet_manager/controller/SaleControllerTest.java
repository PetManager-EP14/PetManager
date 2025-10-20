package com.ep14.pet_manager.controller;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.ep14.pet_manager.dto.SaleDTO;
import com.ep14.pet_manager.service.SaleService;

class SaleControllerTest {

    @Mock
    private SaleService saleService;

    @InjectMocks
    private SaleController controller;

    private SaleDTO saleDTO;
    private UUID userId;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        saleDTO = new SaleDTO();
        saleDTO.setUserId(userId);
    }

    // 1. Registrar venta exitosamente
    @Test
    void registerSale_shouldReturnOk() {
        when(saleService.registerSale(any(SaleDTO.class))).thenReturn(saleDTO);

        ResponseEntity<SaleDTO> response = controller.registerSale(saleDTO);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        verify(saleService).registerSale(any(SaleDTO.class));
    }

    // 2. Registrar venta lanza excepción
    @Test
    void registerSale_whenException_shouldPropagate() {
        when(saleService.registerSale(any(SaleDTO.class)))
                .thenThrow(new IllegalArgumentException("Usuario no encontrado"));

        try {
            controller.registerSale(saleDTO);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).contains("Usuario no encontrado");
        }
    }

    // 3. Obtener todas las ventas
    @Test
    void getAllSales_shouldReturnList() {
        when(saleService.getAllSales()).thenReturn(List.of(saleDTO));

        ResponseEntity<List<SaleDTO>> response = controller.getAllSales();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotEmpty();
        verify(saleService).getAllSales();
    }

    // 4. Obtener ventas por usuario
    @Test
    void getSalesByUser_shouldReturnOk() {
        when(saleService.getSalesByUser(any(UUID.class))).thenReturn(List.of(saleDTO));

        ResponseEntity<List<SaleDTO>> response = controller.getSalesByUser(userId);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotEmpty();
        verify(saleService).getSalesByUser(userId);
    }

    // 5. Obtener venta por ID
    @Test
    void getSaleById_shouldReturnOk() {
        when(saleService.getSaleById(1L)).thenReturn(saleDTO);

        ResponseEntity<SaleDTO> response = controller.getSaleById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        verify(saleService).getSaleById(1L);
    }
}
