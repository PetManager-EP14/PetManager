package com.ep14.pet_manager.dto;

import com.ep14.pet_manager.entity.Sale;
import com.ep14.pet_manager.entity.SaleNotification;
import com.ep14.pet_manager.entity.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SaleNotificationDTOTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        SaleNotificationDTO dto = new SaleNotificationDTO();
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime shippingDate = now.plusDays(2);

        dto.setSaleNotificationId(1L);
        dto.setSaleId(100L);
        dto.setShippingDate(shippingDate);
        dto.setMedia("EMAIL");
        dto.setType("HIGH_VOLUMEN");
        dto.setCreatedAt(now);
        dto.setCustomerName("Juan Pérez");
        dto.setCustomerEmail("juan@example.com");
        dto.setSaleTotal("1500.50");
        dto.setSaleDate(now);

        assertThat(dto.getSaleNotificationId()).isEqualTo(1L);
        assertThat(dto.getSaleId()).isEqualTo(100L);
        assertThat(dto.getShippingDate()).isEqualTo(shippingDate);
        assertThat(dto.getMedia()).isEqualTo("EMAIL");
        assertThat(dto.getType()).isEqualTo("HIGH_VOLUMEN");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getCustomerName()).isEqualTo("Juan Pérez");
        assertThat(dto.getCustomerEmail()).isEqualTo("juan@example.com");
        assertThat(dto.getSaleTotal()).isEqualTo("1500.50");
        assertThat(dto.getSaleDate()).isEqualTo(now);
    }

    @Test
    void testConstructorFromSaleNotificationEntity() {
        // Arrange
        User user = new User();
        user.setName("María García");
        user.setEmail("maria@example.com");

        Sale sale = new Sale();
        sale.setSaleId(200L);
        sale.setUser(user);
        sale.setTotal(new BigDecimal("2500.75"));
        OffsetDateTime saleDate = OffsetDateTime.now();
        sale.setDate(saleDate);

        SaleNotification notification = new SaleNotification();
        notification.setSaleNotificationId(10L);
        notification.setSale(sale);
        OffsetDateTime shippingDate = OffsetDateTime.now().plusDays(3);
        notification.setShippingDate(shippingDate);
        notification.setMedia(SaleNotification.media.SMS);
        notification.setType(SaleNotification.type.HIGH_ROTATION);
        OffsetDateTime createdAt = OffsetDateTime.now();
        notification.setCreatedAt(createdAt);

        // Act
        SaleNotificationDTO dto = new SaleNotificationDTO(notification);

        // Assert
        assertThat(dto.getSaleNotificationId()).isEqualTo(10L);
        assertThat(dto.getSaleId()).isEqualTo(200L);
        assertThat(dto.getShippingDate()).isEqualTo(shippingDate);
        assertThat(dto.getMedia()).isEqualTo("SMS");
        assertThat(dto.getType()).isEqualTo("HIGH_ROTATION");
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
        assertThat(dto.getCustomerName()).isEqualTo("María García");
        assertThat(dto.getCustomerEmail()).isEqualTo("maria@example.com");
        assertThat(dto.getSaleTotal()).isEqualTo("2500.75");
        assertThat(dto.getSaleDate()).isEqualTo(saleDate);
    }

    @Test
    void testConstructorFromSaleNotificationWithNullSale() {
        // Arrange
        OffsetDateTime shippingDate = OffsetDateTime.now();
        OffsetDateTime createdAt = OffsetDateTime.now();

        SaleNotification notification = new SaleNotification();
        notification.setSaleNotificationId(15L);
        notification.setSale(null);
        notification.setShippingDate(shippingDate);
        notification.setMedia(SaleNotification.media.PUSH);
        notification.setType(SaleNotification.type.ANOTHER);
        notification.setCreatedAt(createdAt);

        // Act
        SaleNotificationDTO dto = new SaleNotificationDTO(notification);

        // Assert
        assertThat(dto.getSaleNotificationId()).isEqualTo(15L);
        assertThat(dto.getSaleId()).isNull();
        assertThat(dto.getCustomerName()).isNull();
        assertThat(dto.getCustomerEmail()).isNull();
        assertThat(dto.getSaleTotal()).isNull();
        assertThat(dto.getSaleDate()).isNull();
        assertThat(dto.getShippingDate()).isEqualTo(shippingDate);
        assertThat(dto.getMedia()).isEqualTo("PUSH");
        assertThat(dto.getType()).isEqualTo("ANOTHER");
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void testConstructorFromSaleNotificationWithSaleButNullUser() {
        // Arrange
        Sale sale = new Sale();
        sale.setSaleId(300L);
        sale.setUser(null);
        sale.setTotal(new BigDecimal("500.00"));
        OffsetDateTime saleDate = OffsetDateTime.now();
        sale.setDate(saleDate);

        SaleNotification notification = new SaleNotification();
        notification.setSaleNotificationId(20L);
        notification.setSale(sale);
        notification.setShippingDate(OffsetDateTime.now());
        notification.setMedia(SaleNotification.media.EMAIL);
        notification.setType(SaleNotification.type.HIGH_VOLUMEN);
        notification.setCreatedAt(OffsetDateTime.now());

        // Act
        SaleNotificationDTO dto = new SaleNotificationDTO(notification);

        // Assert
        assertThat(dto.getSaleNotificationId()).isEqualTo(20L);
        assertThat(dto.getSaleId()).isEqualTo(300L);
        assertThat(dto.getSaleTotal()).isEqualTo("500.00");
        assertThat(dto.getSaleDate()).isEqualTo(saleDate);
        assertThat(dto.getCustomerName()).isNull();
        assertThat(dto.getCustomerEmail()).isNull();
    }

    @Test
    void testGettersAndSettersForAllFields() {
        // Arrange
        SaleNotificationDTO dto = new SaleNotificationDTO();
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime shipping = now.plusDays(5);
        OffsetDateTime saleDate = now.minusDays(1);

        // Act & Assert
        dto.setSaleNotificationId(999L);
        assertThat(dto.getSaleNotificationId()).isEqualTo(999L);

        dto.setSaleId(888L);
        assertThat(dto.getSaleId()).isEqualTo(888L);

        dto.setShippingDate(shipping);
        assertThat(dto.getShippingDate()).isEqualTo(shipping);

        dto.setMedia("PUSH");
        assertThat(dto.getMedia()).isEqualTo("PUSH");

        dto.setType("ANOTHER");
        assertThat(dto.getType()).isEqualTo("ANOTHER");

        dto.setCreatedAt(now);
        assertThat(dto.getCreatedAt()).isEqualTo(now);

        dto.setCustomerName("Pedro López");
        assertThat(dto.getCustomerName()).isEqualTo("Pedro López");

        dto.setCustomerEmail("pedro@test.com");
        assertThat(dto.getCustomerEmail()).isEqualTo("pedro@test.com");

        dto.setSaleTotal("9999.99");
        assertThat(dto.getSaleTotal()).isEqualTo("9999.99");

        dto.setSaleDate(saleDate);
        assertThat(dto.getSaleDate()).isEqualTo(saleDate);
    }

    @Test
    void testConstructorFromEntityWithAllMediaTypes() {
        // Test EMAIL
        SaleNotification emailNotification = createNotificationWithMedia(SaleNotification.media.EMAIL);
        SaleNotificationDTO emailDto = new SaleNotificationDTO(emailNotification);
        assertThat(emailDto.getMedia()).isEqualTo("EMAIL");

        // Test SMS
        SaleNotification smsNotification = createNotificationWithMedia(SaleNotification.media.SMS);
        SaleNotificationDTO smsDto = new SaleNotificationDTO(smsNotification);
        assertThat(smsDto.getMedia()).isEqualTo("SMS");

        // Test PUSH
        SaleNotification pushNotification = createNotificationWithMedia(SaleNotification.media.PUSH);
        SaleNotificationDTO pushDto = new SaleNotificationDTO(pushNotification);
        assertThat(pushDto.getMedia()).isEqualTo("PUSH");
    }

    @Test
    void testConstructorFromEntityWithAllNotificationTypes() {
        // Test HIGH_VOLUMEN
        SaleNotification highVolumeNotification = createNotificationWithType(SaleNotification.type.HIGH_VOLUMEN);
        SaleNotificationDTO highVolumeDto = new SaleNotificationDTO(highVolumeNotification);
        assertThat(highVolumeDto.getType()).isEqualTo("HIGH_VOLUMEN");

        // Test HIGH_ROTATION
        SaleNotification highRotationNotification = createNotificationWithType(SaleNotification.type.HIGH_ROTATION);
        SaleNotificationDTO highRotationDto = new SaleNotificationDTO(highRotationNotification);
        assertThat(highRotationDto.getType()).isEqualTo("HIGH_ROTATION");

        // Test ANOTHER
        SaleNotification anotherNotification = createNotificationWithType(SaleNotification.type.ANOTHER);
        SaleNotificationDTO anotherDto = new SaleNotificationDTO(anotherNotification);
        assertThat(anotherDto.getType()).isEqualTo("ANOTHER");
    }

    // Helper methods
    private SaleNotification createNotificationWithMedia(SaleNotification.media media) {
        Sale sale = new Sale();
        sale.setSaleId(1L);

        SaleNotification notification = new SaleNotification();
        notification.setSale(sale);
        notification.setMedia(media);
        notification.setType(SaleNotification.type.HIGH_VOLUMEN);
        notification.setShippingDate(OffsetDateTime.now());
        notification.setCreatedAt(OffsetDateTime.now());

        return notification;
    }

    private SaleNotification createNotificationWithType(SaleNotification.type type) {
        Sale sale = new Sale();
        sale.setSaleId(1L);

        SaleNotification notification = new SaleNotification();
        notification.setSale(sale);
        notification.setMedia(SaleNotification.media.EMAIL);
        notification.setType(type);
        notification.setShippingDate(OffsetDateTime.now());
        notification.setCreatedAt(OffsetDateTime.now());

        return notification;
    }
}
