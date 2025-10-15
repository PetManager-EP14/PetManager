package com.ep14.pet_manager.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import com.ep14.pet_manager.controller.PurchaseController;
import com.ep14.pet_manager.dto.PurchaseDTO;
import com.ep14.pet_manager.service.PurchaseService;

class PurchaseControllerTests {

    @Mock
    PurchaseService purchaseService;

    @InjectMocks
    PurchaseController purchaseController;

    @Test
    void getAllPurchases_returnsList() {
        PurchaseDTO dto = new PurchaseDTO.Builder()
            .id(1L).supplierId(1L).date(OffsetDateTime.now())
            .status(PurchaseDTO.StatusShopping.DRAFT)
            .total(BigDecimal.TEN)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .userId(UUID.randomUUID()).shoppingDetailIds(List.of(1L,2L,3L)).build();

        when(purchaseService.getAllPurchases()).thenReturn(List.of(dto));

        ResponseEntity<List<PurchaseDTO>> response = purchaseController.getAllPurchases();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void createPurchase_statusNull_badRequest() {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setStatus(null);

        ResponseEntity<?> response = purchaseController.createPurchase(dto);
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }
}
