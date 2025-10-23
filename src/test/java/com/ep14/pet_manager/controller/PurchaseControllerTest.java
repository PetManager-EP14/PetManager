package com.ep14.pet_manager.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.ep14.pet_manager.dto.PurchaseDTO;
import com.ep14.pet_manager.service.PurchaseService;

class PurchaseControllerTest {

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController controller;

    private PurchaseDTO purchaseDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        purchaseDTO = new PurchaseDTO();
        purchaseDTO.setId(1L);
        purchaseDTO.setSupplierId(10L);
        purchaseDTO.setUserId(UUID.randomUUID());
        purchaseDTO.setStatus(PurchaseDTO.StatusShopping.DRAFT);
        purchaseDTO.setTotal(new BigDecimal("100.00"));
        purchaseDTO.setDate(OffsetDateTime.now());
    }

    @Test
    void getAllPurchases_shouldReturnOkWithList() {
        when(purchaseService.getAllPurchases()).thenReturn(List.of(purchaseDTO));

        ResponseEntity<List<PurchaseDTO>> response = controller.getAllPurchases();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
        verify(purchaseService).getAllPurchases();
    }

    @Test
    void getPurchaseById_shouldReturnOk() {
        when(purchaseService.getPurchaseById(1L)).thenReturn(purchaseDTO);

        ResponseEntity<PurchaseDTO> response = controller.getPurchaseById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        verify(purchaseService).getPurchaseById(1L);
    }

    @Test
    void createPurchase_shouldReturnOk_whenStatusIsNotNull() {
        when(purchaseService.createPurchase(any(PurchaseDTO.class))).thenReturn(purchaseDTO);

        ResponseEntity<?> response = controller.createPurchase(purchaseDTO);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        verify(purchaseService).createPurchase(any(PurchaseDTO.class));
    }

    @Test
    void createPurchase_shouldReturnBadRequest_whenStatusIsNull() {
        PurchaseDTO purchaseWithoutStatus = new PurchaseDTO();
        purchaseWithoutStatus.setSupplierId(10L);
        purchaseWithoutStatus.setUserId(UUID.randomUUID());
        purchaseWithoutStatus.setStatus(null);

        ResponseEntity<?> response = controller.createPurchase(purchaseWithoutStatus);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("El estado es obligatorio");
        verify(purchaseService, never()).createPurchase(any(PurchaseDTO.class));
    }

    @Test
    void createPurchase_whenException_shouldPropagate() {
        when(purchaseService.createPurchase(any(PurchaseDTO.class)))
                .thenThrow(new IllegalArgumentException("Proveedor no encontrado"));

        assertThatThrownBy(() -> controller.createPurchase(purchaseDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Proveedor no encontrado");
    }

    @Test
    void getPurchaseById_whenNotFound_shouldPropagateException() {
        when(purchaseService.getPurchaseById(999L))
                .thenThrow(new RuntimeException("Compra no encontrada"));

        assertThatThrownBy(() -> controller.getPurchaseById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Compra no encontrada");
    }

    @Test
    void getAllPurchases_shouldInvokeExpectedMethods() {
        when(purchaseService.getAllPurchases()).thenReturn(List.of(purchaseDTO));

        controller.getAllPurchases();

        verify(purchaseService).getAllPurchases();
    }

    @Test
    void createPurchase_shouldInvokeExpectedMethods() {
        when(purchaseService.createPurchase(any(PurchaseDTO.class))).thenReturn(purchaseDTO);

        controller.createPurchase(purchaseDTO);

        verify(purchaseService).createPurchase(any(PurchaseDTO.class));
    }
}

