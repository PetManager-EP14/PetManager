package com.ep14.pet_manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ep14.pet_manager.entity.Product;
import com.ep14.pet_manager.entity.Role;
import com.ep14.pet_manager.entity.Sale;
import com.ep14.pet_manager.entity.SaleDetails;
import com.ep14.pet_manager.entity.SaleNotification;
import com.ep14.pet_manager.entity.User;
import com.ep14.pet_manager.repository.SaleNotificationRepository;

/**
 * Pruebas unitarias para NotificationService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationService Unit Tests")
class NotificationServiceTest {

    @Mock
    private SaleNotificationRepository saleNotificationRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationService notificationService;

    private Sale testSale;
    private User testUser;
    private Product testProduct;
    private SaleNotification testNotification;

    @BeforeEach
    void setUp() {
        // Configurar usuario de prueba
        Role role = new Role();
        role.setRoleId(1L);
        role.setCode("CUSTOMER");

        testUser = new User();
        testUser.setUserId(UUID.randomUUID());
        testUser.setName("Carlos García");
        testUser.setEmail("carlos@example.com");
        testUser.setPhone("555-9876");
        testUser.setAddress("Av. Principal 456");
        testUser.setRole(role);

        // Configurar producto de prueba
        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setName("Alimento Premium para Perros");
        testProduct.setPriceSale(new BigDecimal("150.00"));
        testProduct.setStock(new BigDecimal("50"));

        // Configurar detalle de venta
        SaleDetails saleDetail = new SaleDetails();
        saleDetail.setSaleDetailId(1L);
        saleDetail.setProduct(testProduct);
        saleDetail.setAmount(new BigDecimal("15"));

        // Configurar venta de prueba
        testSale = new Sale();
        testSale.setSaleId(100L);
        testSale.setUser(testUser);
        testSale.setDate(OffsetDateTime.now());
        testSale.setTotal(new BigDecimal("2250.00"));
        testSale.setMethod(Sale.paymentMethod.CARD);
        
        List<SaleDetails> saleDetailsList = new ArrayList<>();
        saleDetailsList.add(saleDetail);
        testSale.setSaleDetails(saleDetailsList);
        saleDetail.setSale(testSale);

        // Configurar notificación de prueba
        testNotification = new SaleNotification();
        testNotification.setSaleNotificationId(1L);
        testNotification.setSale(testSale);
        testNotification.setMedia(SaleNotification.media.EMAIL);
        testNotification.setType(SaleNotification.type.HIGH_VOLUMEN);
        testNotification.setShippingDate(OffsetDateTime.now());
        testNotification.setCreatedAt(OffsetDateTime.now());
    }

    @Test
    @DisplayName("Debe crear y enviar notificación de alto volumen exitosamente")
    void testCreateHighVolumeNotification_Success() {
        // Arrange
        when(saleNotificationRepository.save(any(SaleNotification.class)))
            .thenReturn(testNotification);
        doNothing().when(emailService).sendHighVolumeSaleNotification(any(Sale.class));

        // Act
        SaleNotification result = notificationService.createHighVolumeNotification(testSale);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getSaleNotificationId()).isEqualTo(1L);
        assertThat(result.getSale()).isEqualTo(testSale);
        assertThat(result.getMedia()).isEqualTo(SaleNotification.media.EMAIL);
        assertThat(result.getType()).isEqualTo(SaleNotification.type.HIGH_VOLUMEN);
        assertThat(result.getShippingDate()).isNotNull();
        assertThat(result.getCreatedAt()).isNotNull();

        // Verificar que se guardó la notificación
        verify(saleNotificationRepository, times(1)).save(any(SaleNotification.class));
        
        // Verificar que se envió el email
        verify(emailService, times(1)).sendHighVolumeSaleNotification(testSale);
    }

    @Test
    @DisplayName("Debe guardar notificación aunque falle el envío de email")
    void testCreateHighVolumeNotification_EmailFailure() {
        // Arrange
        when(saleNotificationRepository.save(any(SaleNotification.class)))
            .thenReturn(testNotification);
        doThrow(new RuntimeException("Email service error"))
            .when(emailService).sendHighVolumeSaleNotification(any(Sale.class));

        // Act
        SaleNotification result = notificationService.createHighVolumeNotification(testSale);

        // Assert - La notificación debe guardarse aunque falle el email
        assertThat(result).isNotNull();
        assertThat(result.getSaleNotificationId()).isEqualTo(1L);
        
        // Verificar que se intentó guardar y enviar
        verify(saleNotificationRepository, times(1)).save(any(SaleNotification.class));
        verify(emailService, times(1)).sendHighVolumeSaleNotification(testSale);
    }

    @Test
    @DisplayName("Debe capturar correctamente los datos de la notificación")
    void testCreateHighVolumeNotification_CorrectData() {
        // Arrange
        ArgumentCaptor<SaleNotification> notificationCaptor = 
            ArgumentCaptor.forClass(SaleNotification.class);
        
        when(saleNotificationRepository.save(notificationCaptor.capture()))
            .thenReturn(testNotification);

        // Act
        notificationService.createHighVolumeNotification(testSale);

        // Assert
        SaleNotification capturedNotification = notificationCaptor.getValue();
        assertThat(capturedNotification.getSale()).isEqualTo(testSale);
        assertThat(capturedNotification.getMedia()).isEqualTo(SaleNotification.media.EMAIL);
        assertThat(capturedNotification.getType()).isEqualTo(SaleNotification.type.HIGH_VOLUMEN);
        assertThat(capturedNotification.getShippingDate()).isNotNull();
        assertThat(capturedNotification.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Debe obtener notificaciones por ID de venta")
    void testGetNotificationsBySale() {
        // Arrange
        Long saleId = 100L;
        List<SaleNotification> expectedNotifications = Arrays.asList(
            testNotification,
            createNotification(2L, testSale)
        );
        
        when(saleNotificationRepository.findBySale_SaleId(saleId))
            .thenReturn(expectedNotifications);

        // Act
        List<SaleNotification> result = notificationService.getNotificationsBySale(saleId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSaleNotificationId()).isEqualTo(1L);
        assertThat(result.get(1).getSaleNotificationId()).isEqualTo(2L);
        
        verify(saleNotificationRepository, times(1)).findBySale_SaleId(saleId);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay notificaciones para la venta")
    void testGetNotificationsBySale_Empty() {
        // Arrange
        Long saleId = 999L;
        when(saleNotificationRepository.findBySale_SaleId(saleId))
            .thenReturn(new ArrayList<>());

        // Act
        List<SaleNotification> result = notificationService.getNotificationsBySale(saleId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        
        verify(saleNotificationRepository, times(1)).findBySale_SaleId(saleId);
    }

    @Test
    @DisplayName("Debe obtener notificaciones por tipo HIGH_VOLUMEN")
    void testGetNotificationsByType_HighVolume() {
        // Arrange
        SaleNotification.type type = SaleNotification.type.HIGH_VOLUMEN;
        List<SaleNotification> expectedNotifications = Arrays.asList(
            testNotification,
            createNotification(2L, testSale),
            createNotification(3L, testSale)
        );
        
        when(saleNotificationRepository.findByType(type))
            .thenReturn(expectedNotifications);

        // Act
        List<SaleNotification> result = notificationService.getNotificationsByType(type);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        result.forEach(notification -> 
            assertThat(notification.getType()).isEqualTo(SaleNotification.type.HIGH_VOLUMEN)
        );
        
        verify(saleNotificationRepository, times(1)).findByType(type);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay notificaciones del tipo especificado")
    void testGetNotificationsByType_Empty() {
        // Arrange
        SaleNotification.type type = SaleNotification.type.HIGH_VOLUMEN;
        when(saleNotificationRepository.findByType(type))
            .thenReturn(new ArrayList<>());

        // Act
        List<SaleNotification> result = notificationService.getNotificationsByType(type);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        
        verify(saleNotificationRepository, times(1)).findByType(type);
    }

    @Test
    @DisplayName("Debe manejar múltiples notificaciones para la misma venta")
    void testMultipleNotificationsForSameSale() {
        // Arrange
        SaleNotification notification1 = createNotification(1L, testSale);
        SaleNotification notification2 = createNotification(2L, testSale);
        
        when(saleNotificationRepository.save(any(SaleNotification.class)))
            .thenReturn(notification1)
            .thenReturn(notification2);

        // Act
        SaleNotification result1 = notificationService.createHighVolumeNotification(testSale);
        SaleNotification result2 = notificationService.createHighVolumeNotification(testSale);

        // Assert
        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result1.getSale()).isEqualTo(testSale);
        assertThat(result2.getSale()).isEqualTo(testSale);
        
        verify(saleNotificationRepository, times(2)).save(any(SaleNotification.class));
        verify(emailService, times(2)).sendHighVolumeSaleNotification(testSale);
    }

    /**
     * Método auxiliar para crear notificaciones de prueba
     */
    private SaleNotification createNotification(Long id, Sale sale) {
        SaleNotification notification = new SaleNotification();
        notification.setSaleNotificationId(id);
        notification.setSale(sale);
        notification.setMedia(SaleNotification.media.EMAIL);
        notification.setType(SaleNotification.type.HIGH_VOLUMEN);
        notification.setShippingDate(OffsetDateTime.now());
        notification.setCreatedAt(OffsetDateTime.now());
        return notification;
    }
}
