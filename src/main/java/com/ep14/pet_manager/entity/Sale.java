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
    @Column(name = "sale_id")
    private Long saleId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private OffsetDateTime date = OffsetDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private paymentMethod method;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private saleStatus status = saleStatus.DRAFT;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @OneToOne
    private SaleNotification saleNotification;

    @OneToMany(mappedBy = "sale")
    private List<SaleDetails> saleDetails;

    public Sale() {
    }

    @JsonCreator
    public Sale(@JsonProperty("saleId") Long saleId,
                @JsonProperty("user") User user,
                @JsonProperty("date") OffsetDateTime date,
                @JsonProperty("method") paymentMethod method,
                @JsonProperty("status") saleStatus status,
                @JsonProperty("total") BigDecimal total,
                @JsonProperty("createdAt") OffsetDateTime createdAt,
                @JsonProperty("updatedAt") OffsetDateTime updatedAt,
                @JsonProperty("saleNotification") SaleNotification saleNotification,
                @JsonProperty("saleDetails") List<SaleDetails> saleDetails) {
        this.saleId = saleId;
        this.user = user;
        this.date = date;
        this.method = method;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.saleNotification = saleNotification;
        this.saleDetails = saleDetails;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
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

    public paymentMethod getMethod() {
        return method;
    }

    public void setMethod(paymentMethod method) {
        this.method = method;
    }

    public saleStatus getStatus() {
        return status;
    }

    public void setStatus(saleStatus status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SaleNotification getSaleNotification() {
        return saleNotification;
    }

    public void setSaleNotification(SaleNotification saleNotification) {
        this.saleNotification = saleNotification;
    }

    public List<SaleDetails> getSaleDetails() {
        return saleDetails;
    }

    public void setSaleDetails(List<SaleDetails> saleDetails) {
        this.saleDetails = saleDetails;
    }

    public enum paymentMethod {
        CHASH, CARD, TRANSFER, CREDIT;
    }

    public enum saleStatus {
        DRAFT, REGISTERED, ANNULLED;
    }
}
