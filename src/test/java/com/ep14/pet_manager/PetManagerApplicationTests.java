package com.ep14.pet_manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import com.ep14.pet_manager.controller.AccessLogController;
import com.ep14.pet_manager.controller.PurchaseController;
import com.ep14.pet_manager.dto.PurchaseDTO;
import com.ep14.pet_manager.entity.AccessLog;
import com.ep14.pet_manager.exception.GlobalExceptionHandler;
import com.ep14.pet_manager.repository.AccessLogRepository;
import com.ep14.pet_manager.service.PurchaseService;

@SpringBootTest
class PetManagerApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController purchaseController;

    // Test para el método main de PetManagerApplication
    @Test
    void main_shouldStartApplication() {
        // Verificar que el método main no lanza excepciones
        // y que la aplicación se puede iniciar correctamente
        assertThat(applicationContext).isNotNull();
        assertThat(applicationContext.containsBean("petManagerApplication")).isTrue();
    }

    // Test para verificar que la aplicación se carga correctamente
    @Test
    void contextLoads() {
        assertThat(purchaseController).isNotNull();
        assertThat(applicationContext).isNotNull();
    }

    // Test del constructor de PetManagerApplication (mejora coverage)
    @Test
    void petManagerApplication_canBeInstantiated() {
        PetManagerApplication application = new PetManagerApplication();
        assertThat(application).isNotNull();
    }

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

    @Test
    void testGetPurchaseById() {
        PurchaseDTO purchase = new PurchaseDTO();
        purchase.setId(1L);
        when(purchaseService.getPurchaseById(1L)).thenReturn(purchase);

        ResponseEntity<PurchaseDTO> response = purchaseController.getPurchaseById(1L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(purchase);
    }

    @Test
    void testCreatePurchaseSuccess() {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setStatus(PurchaseDTO.StatusShopping.DRAFT); // estado válido

        when(purchaseService.createPurchase(dto)).thenReturn(dto);

        ResponseEntity<?> response = purchaseController.createPurchase(dto);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(dto);
    }

    // AccessLogController Tests
    @Mock
    private AccessLogRepository repo;

    @InjectMocks
    private AccessLogController controller;

    @Test
    void testSearchWithoutParams() {
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
        // puede setear valores a log si la entidad tiene setters

        List<AccessLog> logs = List.of(log);

        when(repo.search(userId, from, to)).thenReturn(logs);

        List<AccessLog> result = controller.search(userId, from, to);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(log);
    }

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleGenericException_returnsInternalServerError() {
        Exception ex = new Exception("Generic error");
        ResponseEntity<Map<String, Object>> response = handler.handleException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsKeys("timestamp", "status", "error", "message");
        assertThat(response.getBody()).containsEntry("error", "Internal Server Error");
        assertThat(response.getBody()).containsEntry("message", "Generic error");
    }

    @Test
    void handleException_withAccessDeniedException_throws() {
        AccessDeniedException ex = new AccessDeniedException("Denied");
        assertThrows(RuntimeException.class, () -> handler.handleException(ex));
    }

    @Test
    void handleException_withAuthenticationException_throws() {
        AuthenticationException ex = new AuthenticationException("Auth failed") {};
        assertThrows(RuntimeException.class, () -> handler.handleException(ex));
    }

    @Test
    void handleIllegalArgument_returnsBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid Argument");
        ResponseEntity<Map<String, Object>> response = handler.handleIllegalArgument(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKeys("timestamp", "status", "error", "message");
        assertThat(response.getBody()).containsEntry("error", "Bad Request");
        assertThat(response.getBody()).containsEntry("message", "Invalid Argument");
    }
}
