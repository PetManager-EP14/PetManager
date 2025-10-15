package com.ep14.pet_manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.ep14.pet_manager.controller.AccessLogController;
import com.ep14.pet_manager.controller.PurchaseController;
import com.ep14.pet_manager.dto.PurchaseDTO;
import com.ep14.pet_manager.entity.AccessLog;
import com.ep14.pet_manager.repository.AccessLogRepository;
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
        PurchaseDTO purchase = new PurchaseDTO.Builder()
                .id(1L)
                .supplierId(123L)
                .date(OffsetDateTime.now())
                .status(PurchaseDTO.StatusShopping.DRAFT)
                .total(new BigDecimal("100.00"))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .userId(UUID.randomUUID())
                .shoppingDetailIds(List.of(1L, 2L, 3L))
                .build();

        when(purchaseService.getAllPurchases()).thenReturn(List.of(purchase));

        ResponseEntity<List<PurchaseDTO>> response = purchaseController.getAllPurchases();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getTotal()).isEqualByComparingTo("100.00");
    }

    @Test
    void testCreatePurchaseBadRequest() {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setStatus(null); // status es obligatorio

        ResponseEntity<?> response = purchaseController.createPurchase(dto);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(response.getBody()).isEqualTo("El estado es obligatorio");
    }

    @Mock
    private AccessLogRepository repo;
    
    @InjectMocks
    private AccessLogController controller;

    @Test
    void testSearchWithoutParams(){
        List<AccessLog> logs = Collections.emptyList();
        when(repo.search(null, null, null)).thenReturn(logs);

        List<AccessLog> result = controller.search(null, null, null);

        assertThat(result).isEqualTo(logs);
    }

    @Test
    void testSearchWithParams() {
        UUID userId = UUID.randomUUID();
        Instant from = Instant.now().minusSeconds(3600);
        Instant to = Instant.now();

        AccessLog log = new AccessLog();
        // puedes setearle valores a log si tu entidad tiene setters

        List<AccessLog> logs = List.of(log);

        when(repo.search(userId, from, to)).thenReturn(logs);

        List<AccessLog> result = controller.search(userId, from, to);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(log);
    }

}
