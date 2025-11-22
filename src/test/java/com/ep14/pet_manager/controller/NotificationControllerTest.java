package com.ep14.pet_manager.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ep14.pet_manager.assembler.NotificationModelAssembler;
import com.ep14.pet_manager.entity.Sale;
import com.ep14.pet_manager.entity.SaleNotification;
import com.ep14.pet_manager.service.NotificationService;

class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationModelAssembler assembler;

    private NotificationController notificationController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        notificationController = new NotificationController(notificationService, assembler);
    }

    // 1. Obtener notificaciones por venta - retorna lista de notificaciones
    @Test
    void getNotificationsBySale_shouldReturnNotifications_whenSaleExists() {
        // Preparar datos de prueba
        Long saleId = 1L;
        Sale sale = new Sale();
        sale.setSaleId(saleId);

        SaleNotification notification1 = new SaleNotification(
            1L,
            sale,
            OffsetDateTime.now(),
            SaleNotification.media.EMAIL,
            SaleNotification.type.HIGH_VOLUMEN,
            OffsetDateTime.now()
        );

        SaleNotification notification2 = new SaleNotification(
            2L,
            sale,
            OffsetDateTime.now(),
            SaleNotification.media.SMS,
            SaleNotification.type.HIGH_ROTATION,
            OffsetDateTime.now()
        );

        List<SaleNotification> expectedNotifications = Arrays.asList(notification1, notification2);

        // Configurar mock
        when(notificationService.getNotificationsBySale(saleId))
            .thenReturn(expectedNotifications);
        when(assembler.toCollectionModel(any()))
            .thenReturn(CollectionModel.of(List.of(EntityModel.of(notification1), EntityModel.of(notification2))));

        // Ejecutar
        ResponseEntity<CollectionModel<EntityModel<SaleNotification>>> response = 
            notificationController.getNotificationsBySale(saleId);

        // Verificar
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(2);
        
        verify(notificationService).getNotificationsBySale(saleId);
    }

    // 2. Obtener notificaciones por venta - retorna lista vacía cuando no hay notificaciones
    @Test
    void getNotificationsBySale_shouldReturnEmptyList_whenNoNotificationsExist() {
        // Preparar datos
        Long saleId = 999L;

        // Configurar mock
        when(notificationService.getNotificationsBySale(saleId))
            .thenReturn(Collections.emptyList());
        when(assembler.toCollectionModel(any()))
            .thenReturn(CollectionModel.empty());

        // Ejecutar
        ResponseEntity<CollectionModel<EntityModel<SaleNotification>>> response = 
            notificationController.getNotificationsBySale(saleId);

        // Verificar
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).isEmpty();
        
        verify(notificationService).getNotificationsBySale(saleId);
    }

    // 3. Obtener notificaciones por tipo HIGH_VOLUMEN
    @Test
    void getNotificationsByType_shouldReturnNotifications_whenTypeIsHighVolumen() {
        // Preparar datos
        Sale sale = new Sale();
        sale.setSaleId(1L);

        SaleNotification notification1 = new SaleNotification(
            1L,
            sale,
            OffsetDateTime.now(),
            SaleNotification.media.EMAIL,
            SaleNotification.type.HIGH_VOLUMEN,
            OffsetDateTime.now()
        );

        SaleNotification notification2 = new SaleNotification(
            2L,
            sale,
            OffsetDateTime.now(),
            SaleNotification.media.PUSH,
            SaleNotification.type.HIGH_VOLUMEN,
            OffsetDateTime.now()
        );

        List<SaleNotification> expectedNotifications = Arrays.asList(notification1, notification2);

        // Configurar mock
        when(notificationService.getNotificationsByType(SaleNotification.type.HIGH_VOLUMEN))
            .thenReturn(expectedNotifications);
        when(assembler.toCollectionModel(any()))
            .thenReturn(CollectionModel.of(List.of(EntityModel.of(notification1), EntityModel.of(notification2))));

        // Ejecutar
        ResponseEntity<CollectionModel<EntityModel<SaleNotification>>> response = 
            notificationController.getNotificationsByType(SaleNotification.type.HIGH_VOLUMEN);

        // Verificar
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(2);
        
        verify(notificationService).getNotificationsByType(SaleNotification.type.HIGH_VOLUMEN);
    }

    // 4. Obtener notificaciones por tipo HIGH_ROTATION
    @Test
    void getNotificationsByType_shouldReturnNotifications_whenTypeIsHighRotation() {
        // Preparar datos
        Sale sale = new Sale();
        sale.setSaleId(2L);

        SaleNotification notification = new SaleNotification(
            3L,
            sale,
            OffsetDateTime.now(),
            SaleNotification.media.SMS,
            SaleNotification.type.HIGH_ROTATION,
            OffsetDateTime.now()
        );

        List<SaleNotification> expectedNotifications = Arrays.asList(notification);

        // Configurar mock
        when(notificationService.getNotificationsByType(SaleNotification.type.HIGH_ROTATION))
            .thenReturn(expectedNotifications);
        when(assembler.toCollectionModel(any()))
            .thenReturn(CollectionModel.of(List.of(EntityModel.of(notification))));

        // Ejecutar
        ResponseEntity<CollectionModel<EntityModel<SaleNotification>>> response = 
            notificationController.getNotificationsByType(SaleNotification.type.HIGH_ROTATION);

        // Verificar
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(1);
        
        verify(notificationService).getNotificationsByType(SaleNotification.type.HIGH_ROTATION);
    }

    // 5. Obtener notificaciones sin especificar tipo - retorna HIGH_VOLUMEN por defecto
    @Test
    void getNotificationsByType_shouldReturnHighVolumenNotifications_whenTypeIsNull() {
        // Preparar datos
        Sale sale = new Sale();
        sale.setSaleId(1L);

        SaleNotification notification = new SaleNotification(
            1L,
            sale,
            OffsetDateTime.now(),
            SaleNotification.media.EMAIL,
            SaleNotification.type.HIGH_VOLUMEN,
            OffsetDateTime.now()
        );

        List<SaleNotification> expectedNotifications = Arrays.asList(notification);

        // Configurar mock
        when(notificationService.getNotificationsByType(SaleNotification.type.HIGH_VOLUMEN))
            .thenReturn(expectedNotifications);
        when(assembler.toCollectionModel(any()))
            .thenReturn(CollectionModel.of(List.of(EntityModel.of(notification))));

        // Ejecutar con tipo null
        ResponseEntity<CollectionModel<EntityModel<SaleNotification>>> response = 
            notificationController.getNotificationsByType(null);

        // Verificar
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(1);
        
        verify(notificationService).getNotificationsByType(SaleNotification.type.HIGH_VOLUMEN);
    }

    // 6. Obtener notificaciones por tipo - retorna lista vacía cuando no hay del tipo especificado
    @Test
    void getNotificationsByType_shouldReturnEmptyList_whenNoNotificationsOfTypeExist() {
        // Configurar mock
        when(notificationService.getNotificationsByType(SaleNotification.type.ANOTHER))
            .thenReturn(Collections.emptyList());
        when(assembler.toCollectionModel(any()))
            .thenReturn(CollectionModel.empty());

        // Ejecutar
        ResponseEntity<CollectionModel<EntityModel<SaleNotification>>> response = 
            notificationController.getNotificationsByType(SaleNotification.type.ANOTHER);

        // Verificar
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).isEmpty();
        
        verify(notificationService).getNotificationsByType(SaleNotification.type.ANOTHER);
    }

    // 7. Verificar que el servicio se llama correctamente con diferentes tipos de media
    @Test
    void getNotificationsBySale_shouldReturnNotificationsWithDifferentMedia() {
        // Preparar datos
        Long saleId = 5L;
        Sale sale = new Sale();
        sale.setSaleId(saleId);

        SaleNotification emailNotification = new SaleNotification(
            1L,
            sale,
            OffsetDateTime.now(),
            SaleNotification.media.EMAIL,
            SaleNotification.type.HIGH_VOLUMEN,
            OffsetDateTime.now()
        );

        SaleNotification smsNotification = new SaleNotification(
            2L,
            sale,
            OffsetDateTime.now(),
            SaleNotification.media.SMS,
            SaleNotification.type.HIGH_VOLUMEN,
            OffsetDateTime.now()
        );

        SaleNotification pushNotification = new SaleNotification(
            3L,
            sale,
            OffsetDateTime.now(),
            SaleNotification.media.PUSH,
            SaleNotification.type.HIGH_VOLUMEN,
            OffsetDateTime.now()
        );

        List<SaleNotification> expectedNotifications = 
            Arrays.asList(emailNotification, smsNotification, pushNotification);

        // Configurar mock
        when(notificationService.getNotificationsBySale(saleId))
            .thenReturn(expectedNotifications);
        when(assembler.toCollectionModel(any()))
            .thenReturn(CollectionModel.of(List.of(
                EntityModel.of(emailNotification),
                EntityModel.of(smsNotification),
                EntityModel.of(pushNotification)
            )));

        // Ejecutar
        ResponseEntity<CollectionModel<EntityModel<SaleNotification>>> response = 
            notificationController.getNotificationsBySale(saleId);

        // Verificar
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(3);
    }
}
