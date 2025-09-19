package com.ep14.pet_manager.entity;

import java.math.BigInteger;
import java.time.OffsetDateTime;

import org.springframework.data.domain.OffsetScrollPosition;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sale_notifications")
public class sale_notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sale_notification_id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private sale sale;

    @Column(nullable = false)
    private OffsetDateTime shipping_date = OffsetDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private media media;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private type type;

    @Column(nullable = false)
    private OffsetDateTime created_at = OffsetDateTime.now();

    public sale_notification() {

    }

    @JsonCreator
    public sale_notification(@JsonProperty("sale_notification_id") Long sale_notification_id,
            @JsonProperty("sale") com.ep14.pet_manager.entity.sale sale,
            @JsonProperty("shipping_date") OffsetDateTime shipping_date,
            @JsonProperty("media") com.ep14.pet_manager.entity.sale_notification.media media,
            @JsonProperty("type") com.ep14.pet_manager.entity.sale_notification.type type,
            @JsonProperty("created_at") OffsetDateTime created_at) {
        this.sale_notification_id = sale_notification_id;
        this.sale = sale;
        this.shipping_date = shipping_date;
        this.media = media;
        this.type = type;
        this.created_at = created_at;
    }

    

    public Long getSale_notification_id() {
        return sale_notification_id;
    }

    public void setSale_notification_id(Long sale_notification_id) {
        this.sale_notification_id = sale_notification_id;
    }

    public sale getSale() {
        return sale;
    }

    public void setSale(sale sale) {
        this.sale = sale;
    }

    public OffsetDateTime getShipping_date() {
        return shipping_date;
    }

    public void setShipping_date(OffsetDateTime shipping_date) {
        this.shipping_date = shipping_date;
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

    public OffsetDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(OffsetDateTime created_at) {
        this.created_at = created_at;
    }



    public enum media {
        email, sms, push
    }

    public enum type {
        high_volumen, high_rotation, another
    }
}
