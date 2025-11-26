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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import com.ep14.pet_manager.assembler.SaleModelAssembler;
import com.ep14.pet_manager.dto.SaleDTO;
import com.ep14.pet_manager.service.SaleService;

class SaleControllerTest {

    @Mock
    private SaleService saleService;

    @Mock
    private SaleModelAssembler assembler;

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

        when(assembler.toModel(any(SaleDTO.class)))
            .thenReturn(EntityModel.of(saleDTO));

        ResponseEntity<EntityModel<SaleDTO>> response = controller.registerSale(saleDTO);

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

        when(assembler.toCollectionModel(any()))
            .thenReturn(CollectionModel.of(List.of(
                EntityModel.of(saleDTO)
            )));

        ResponseEntity<CollectionModel<EntityModel<SaleDTO>>> response = controller.getAllSales();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getContent()).isNotEmpty();
        verify(saleService).getAllSales();
    }

    // 4. Obtener ventas por usuario
    @Test
    void getSalesByUser_shouldReturnOk() {
        when(saleService.getSalesByUser(any(UUID.class))).thenReturn(List.of(saleDTO));

        when(assembler.toCollectionModel(any()))
            .thenReturn(CollectionModel.of(List.of(
                EntityModel.of(saleDTO)
            )));

        ResponseEntity<CollectionModel<EntityModel<SaleDTO>>> response = controller.getSalesByUser(userId);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getContent()).isNotEmpty();
        verify(saleService).getSalesByUser(userId);
    }

    // 5. Obtener venta por ID
    @Test
    void getSaleById_shouldReturnOk() {
        when(saleService.getSaleById(1L)).thenReturn(saleDTO);

        when(assembler.toModel(any(SaleDTO.class)))
            .thenReturn(EntityModel.of(saleDTO));

        ResponseEntity<EntityModel<SaleDTO>> response = controller.getSaleById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        verify(saleService).getSaleById(1L);
    }

    // 6. Obtener ventas filtradas (por usuario, fechas o ID)
    @Test
    void getAllSalesFiltered_shouldReturnOk() {
        when(saleService.getAllSalesFiltered(any(), any(), any(), any()))
            .thenReturn(List.of(saleDTO));
        
        when(assembler.toCollectionModel(any()))
            .thenReturn(CollectionModel.of(List.of(
                EntityModel.of(saleDTO)
            )));

        ResponseEntity<CollectionModel<EntityModel<SaleDTO>>> response = controller.getAllSalesFiltered(
                userId,
                "2025-10-01T00:00:00Z",
                "2025-10-19T00:00:00Z",
                1L
        );

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getContent()).isNotEmpty();
        verify(saleService).getAllSalesFiltered(any(), any(), any(), any());
    }
}
