package com.ep14.pet_manager;

import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.ep14.pet_manager.controller.PurchaseController;
import com.ep14.pet_manager.dto.PurchaseDTO;
import com.ep14.pet_manager.service.PurchaseService;

@SpringBootTest
class PetManagerApplicationTests {

	@Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController purchaseController;

    @Test
    void contextLoads() {
        assertThat(purchaseController).isNotNull();
    }

    @SuppressWarnings("null")
    @Test
    void testGetAllPurchases() {
        PurchaseDTO dto = new PurchaseDTO(
                1L,
                2L,
                OffsetDateTime.now(),
                PurchaseDTO.StatusShopping.DRAFT,
                new BigDecimal("100.50"),
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                UUID.randomUUID(),
                null
        );

        when(purchaseService.getAllPurchases()).thenReturn(List.of(dto));

        ResponseEntity<List<PurchaseDTO>> response = purchaseController.getAllPurchases();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getTotal()).isEqualByComparingTo("100.50");
    }

    @Test
    void testCreatePurchaseBadRequest() {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setStatus(null); // status es obligatorio

        ResponseEntity<?> response = purchaseController.createPurchase(dto);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(response.getBody()).isEqualTo("El estado es obligatorio");
    }

}
