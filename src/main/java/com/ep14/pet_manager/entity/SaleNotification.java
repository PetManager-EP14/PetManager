package com.ep14.pet_manager.entity;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sale_notifications")
public class SaleNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_notification_id")
    private Long saleNotificationId;

    @OneToOne
    @JoinColumn(nullable = false)
    private Sale sale;

    @Column(name = "shipping_date", nullable = false)
    private OffsetDateTime shippingDate = OffsetDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private media media;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private type type;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public SaleNotification() {

    }

    @JsonCreator
    public SaleNotification(@JsonProperty("saleNotificationId") Long saleNotificationId,
                            @JsonProperty("sale") Sale sale,
                            @JsonProperty("shippingDate") OffsetDateTime shippingDate,
                            @JsonProperty("media") SaleNotification.media media,
                            @JsonProperty("type") SaleNotification.type type,
                            @JsonProperty("createdAt") OffsetDateTime createdAt) {
        this.saleNotificationId = saleNotificationId;
        this.sale = sale;
        this.shippingDate = shippingDate;
        this.media = media;
        this.type = type;
        this.createdAt = createdAt;
    }

    public Long getSaleNotificationId() {
        return saleNotificationId;
    }

    public void setSaleNotificationId(Long saleNotificationId) {
        this.saleNotificationId = saleNotificationId;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public OffsetDateTime getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(OffsetDateTime shippingDate) {
        this.shippingDate = shippingDate;
    }

    public media getMedia() {
        return media;
    }

    public void setMedia(media media) {
        this.media = media;
    }

    public type getType() {
        return type;
    }

    public void setType(type type) {
        this.type = type;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public enum media {
        email, sms, push
    }

    public enum type {
        high_volumen, high_rotation, another
    }
}
