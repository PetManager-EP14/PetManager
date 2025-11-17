package com.ep14.pet_manager.dto;

import java.time.OffsetDateTime;

import com.ep14.pet_manager.entity.SaleNotification;

/**
 * DTO para las notificaciones de ventas
 */
public class SaleNotificationDTO {
    
    private Long saleNotificationId;
    private Long saleId;
    private OffsetDateTime shippingDate;
    private String media;
    private String type;
    private OffsetDateTime createdAt;
    
    // Información adicional de la venta
    private String customerName;
    private String customerEmail;
    private String saleTotal;
    private OffsetDateTime saleDate;

    public SaleNotificationDTO() {
    }

    public SaleNotificationDTO(SaleNotification notification) {
        this.saleNotificationId = notification.getSaleNotificationId();
        this.shippingDate = notification.getShippingDate();
        this.media = notification.getMedia().toString();
        this.type = notification.getType().toString();
        this.createdAt = notification.getCreatedAt();
        
        if (notification.getSale() != null) {
            this.saleId = notification.getSale().getSaleId();
            this.saleTotal = notification.getSale().getTotal() != null ? notification.getSale().getTotal().toString() : null;
            this.saleDate = notification.getSale().getDate();
            if (notification.getSale().getUser() != null) {
                this.customerName = notification.getSale().getUser().getName();
                this.customerEmail = notification.getSale().getUser().getEmail();
            }
        }
    }

    // Getters y Setters
    public Long getSaleNotificationId() {
        return saleNotificationId;
    }

    public void setSaleNotificationId(Long saleNotificationId) {
        this.saleNotificationId = saleNotificationId;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public OffsetDateTime getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(OffsetDateTime shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(String saleTotal) {
        this.saleTotal = saleTotal;
    }

    public OffsetDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(OffsetDateTime saleDate) {
        this.saleDate = saleDate;
    }
}
