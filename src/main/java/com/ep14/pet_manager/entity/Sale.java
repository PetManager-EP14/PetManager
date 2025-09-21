package com.ep14.pet_manager.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sale_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private OffsetDateTime date = OffsetDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private payment_method method;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private sale_status status = sale_status.DRAFT;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(nullable = false)
    private OffsetDateTime created_at = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime updated_at = OffsetDateTime.now();

    @OneToOne
    private SaleNotification SaleNotification;

    @OneToMany(mappedBy = "sale")
    private List<SaleDetails> SaleDetails;

    public Sale() {
    }

    @JsonCreator
    public Sale(@JsonProperty("sale_id") Long sale_id,
                @JsonProperty("user") User user,
                @JsonProperty("date") OffsetDateTime date,
                @JsonProperty("method") payment_method method,
                @JsonProperty("status") sale_status status,
                @JsonProperty("total") BigDecimal total,
                @JsonProperty("created_at") OffsetDateTime created_at,
                @JsonProperty("updated_at") OffsetDateTime updated_at,
                @JsonProperty("sale_notification") SaleNotification SaleNotification,
                @JsonProperty("sale_details") List<SaleDetails> SaleDetails) {
        this.sale_id = sale_id;
        this.user = user;
        this.date = date;
        this.method = method;
        this.status = status;
        this.total = total;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.SaleNotification = SaleNotification;
        this.SaleDetails = SaleDetails;
    }

    public Long getSale_id() {
        return sale_id;
    }

    public void setSale_id(Long sale_id) {
        this.sale_id = sale_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public payment_method getMethod() {
        return method;
    }

    public void setMethod(payment_method method) {
        this.method = method;
    }

    public sale_status getStatus() {
        return status;
    }

    public void setStatus(sale_status status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public OffsetDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(OffsetDateTime created_at) {
        this.created_at = created_at;
    }

    public OffsetDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(OffsetDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public SaleNotification getSale_notification() {
        return SaleNotification;
    }

    public void setSale_notification(SaleNotification SaleNotification) {
        this.SaleNotification = SaleNotification;
    }

    public List<SaleDetails> getSale_details() {
        return SaleDetails;
    }

    public void setSale_details(List<SaleDetails> SaleDetails) {
        this.SaleDetails = SaleDetails;
    }

    public enum payment_method {
        CHASH, CARD, TRANSFER, CREDIT;
    }

    public enum sale_status {
        DRAFT, REGISTERED, ANNULLED;
    }
}
