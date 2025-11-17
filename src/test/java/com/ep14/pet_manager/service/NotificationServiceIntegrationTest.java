package com.ep14.pet_manager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.ep14.pet_manager.dto.SaleDTO;
import com.ep14.pet_manager.dto.SaleDetailDTO;
import com.ep14.pet_manager.entity.Product;
import com.ep14.pet_manager.entity.Role;
import com.ep14.pet_manager.entity.Sale;
import com.ep14.pet_manager.entity.SaleDetails;
import com.ep14.pet_manager.entity.User;
import com.ep14.pet_manager.mapper.SaleMapper;
import com.ep14.pet_manager.repository.ProductRepository;
import com.ep14.pet_manager.repository.SaleRepository;
import com.ep14.pet_manager.repository.UserRepository;

/**
 * Pruebas unitarias para la funcionalidad de notificaciones de alto volumen
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceIntegrationTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SaleMapper saleMapper;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SaleService saleService;

    private User testUser;
    private Product testProduct;
    private Role testRole;

    @BeforeEach
    void setUp() {
        // Configurar el umbral de alto volumen para las pruebas
        ReflectionTestUtils.setField(saleService, "highVolumeThreshold", 10);

        // Crear usuario de prueba
        testRole = new Role();
        testRole.setRoleId(1L);

        testUser = new User();
        testUser.setUserId(UUID.randomUUID());
        testUser.setName("Juan Pérez");
        testUser.setEmail("juan@example.com");
        testUser.setPhone("555-1234");
        testUser.setAddress("Calle Falsa 123");
        testUser.setRole(testRole);

        // Crear producto de prueba
        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setName("Alimento para Perros");
        testProduct.setPriceSale(new BigDecimal("125.00"));
        testProduct.setStock(new BigDecimal("100"));
    }

    @Test
    void testRegisterSale_BelowThreshold_NoNotification() {
        // Arrange
        SaleDTO saleDTO = createSaleDTO(5); // Cantidad menor al umbral
        Sale sale = createSale(5);

        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
        when(productRepository.findById(any())).thenReturn(Optional.of(testProduct));
        when(saleMapper.toEntity(any(SaleDTO.class))).thenReturn(sale);
        when(saleRepository.saveAndFlush(any())).thenReturn(sale);
        when(saleMapper.toDTO(any(Sale.class))).thenReturn(saleDTO);

        // Act
        SaleDTO result = saleService.registerSale(saleDTO);

        // Assert
        assertNotNull(result);
        verify(notificationService, never()).createHighVolumeNotification(any());
    }

    @Test
    void testRegisterSale_AboveThreshold_CreatesNotification() {
        // Arrange
        SaleDTO saleDTO = createSaleDTO(15); // Cantidad mayor al umbral
        Sale sale = createSale(15);

        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
        when(productRepository.findById(any())).thenReturn(Optional.of(testProduct));
        when(saleMapper.toEntity(any(SaleDTO.class))).thenReturn(sale);
        when(saleRepository.saveAndFlush(any())).thenReturn(sale);
        when(saleMapper.toDTO(any(Sale.class))).thenReturn(saleDTO);

        // Act
        SaleDTO result = saleService.registerSale(saleDTO);

        // Assert
        assertNotNull(result);
        verify(notificationService, times(1)).createHighVolumeNotification(any(Sale.class));
    }

    @Test
    void testRegisterSale_ExactlyAtThreshold_CreatesNotification() {
        // Arrange
        SaleDTO saleDTO = createSaleDTO(10); // Cantidad exactamente en el umbral
        Sale sale = createSale(10);

        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
        when(productRepository.findById(any())).thenReturn(Optional.of(testProduct));
        when(saleMapper.toEntity(any(SaleDTO.class))).thenReturn(sale);
        when(saleRepository.saveAndFlush(any())).thenReturn(sale);
        when(saleMapper.toDTO(any(Sale.class))).thenReturn(saleDTO);

        // Act
        SaleDTO result = saleService.registerSale(saleDTO);

        // Assert
        assertNotNull(result);
        // La notificación debe crearse cuando la cantidad es mayor o igual al umbral (>=10)
        verify(notificationService, times(1)).createHighVolumeNotification(any(Sale.class));
    }

    @Test
    void testRegisterSale_MultipleProducts_SumsCorrectly() {
        // Arrange
        SaleDTO saleDTO = createSaleDTOWithMultipleProducts();
        Sale sale = createSaleWithMultipleProducts();

        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
        when(productRepository.findById(any())).thenReturn(Optional.of(testProduct));
        when(saleMapper.toEntity(any(SaleDTO.class))).thenReturn(sale);
        when(saleRepository.saveAndFlush(any())).thenReturn(sale);
        when(saleMapper.toDTO(any(Sale.class))).thenReturn(saleDTO);

        // Act
        SaleDTO result = saleService.registerSale(saleDTO);

        // Assert
        assertNotNull(result);
        // Total: 7 + 8 = 15 > 10, debería crear notificación
        verify(notificationService, times(1)).createHighVolumeNotification(any(Sale.class));
    }

    @Test
    void testRegisterSale_NotificationFails_SaleContinues() {
        // Arrange
        SaleDTO saleDTO = createSaleDTO(15);
        Sale sale = createSale(15);

        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
        when(productRepository.findById(any())).thenReturn(Optional.of(testProduct));
        when(saleMapper.toEntity(any(SaleDTO.class))).thenReturn(sale);
        when(saleRepository.saveAndFlush(any())).thenReturn(sale);
        when(saleMapper.toDTO(any(Sale.class))).thenReturn(saleDTO);
        when(notificationService.createHighVolumeNotification(any()))
                .thenThrow(new RuntimeException("Email service error"));

        // Act & Assert
        // La venta debe completarse aunque falle la notificación
        assertDoesNotThrow(() -> saleService.registerSale(saleDTO));
    }

    // Métodos auxiliares

    private SaleDTO createSaleDTO(int quantity) {
        SaleDTO dto = new SaleDTO();
        dto.setUserId(testUser.getUserId());
        dto.setMethod("CASH");

        SaleDetailDTO detailDTO = new SaleDetailDTO();
        detailDTO.setProductId(1L);
        detailDTO.setAmount(new BigDecimal(quantity));

        dto.setDetails(List.of(detailDTO));
        return dto;
    }

    private Sale createSale(int quantity) {
        Sale sale = new Sale();
        sale.setSaleId(1L);
        sale.setUser(testUser);
        sale.setMethod(Sale.paymentMethod.CASH);
        sale.setDate(OffsetDateTime.now());
        sale.setStatus(Sale.saleStatus.REGISTERED);
        sale.setTotal(testProduct.getPriceSale().multiply(new BigDecimal(quantity)));

        SaleDetails detail = new SaleDetails();
        detail.setProduct(testProduct);
        detail.setAmount(new BigDecimal(quantity));
        detail.setSale(sale);

        sale.setSaleDetails(new ArrayList<>(List.of(detail)));
        return sale;
    }

    private SaleDTO createSaleDTOWithMultipleProducts() {
        SaleDTO dto = new SaleDTO();
        dto.setUserId(testUser.getUserId());
        dto.setMethod("CASH");

        SaleDetailDTO detail1 = new SaleDetailDTO();
        detail1.setProductId(1L);
        detail1.setAmount(new BigDecimal("7"));

        SaleDetailDTO detail2 = new SaleDetailDTO();
        detail2.setProductId(2L);
        detail2.setAmount(new BigDecimal("8"));

        dto.setDetails(List.of(detail1, detail2));
        return dto;
    }

    private Sale createSaleWithMultipleProducts() {
        Sale sale = new Sale();
        sale.setSaleId(1L);
        sale.setUser(testUser);
        sale.setMethod(Sale.paymentMethod.CASH);
        sale.setDate(OffsetDateTime.now());
        sale.setStatus(Sale.saleStatus.REGISTERED);

        SaleDetails detail1 = new SaleDetails();
        detail1.setProduct(testProduct);
        detail1.setAmount(new BigDecimal("7"));
        detail1.setSale(sale);

        SaleDetails detail2 = new SaleDetails();
        detail2.setProduct(testProduct);
        detail2.setAmount(new BigDecimal("8"));
        detail2.setSale(sale);

        sale.setSaleDetails(new ArrayList<>(List.of(detail1, detail2)));
        sale.setTotal(testProduct.getPriceSale().multiply(new BigDecimal("15")));

        return sale;
    }
}
