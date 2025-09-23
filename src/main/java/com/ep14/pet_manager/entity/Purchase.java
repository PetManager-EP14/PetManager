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
import jakarta.persistence.Table;

@Entity
@Table(name = "shoppings")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopping_id")
    private Long purchaseId;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private OffsetDateTime date = OffsetDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private statusShopping status = statusShopping.DRAFT;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at",nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "purchase")
    private List<PurchaseDetails> PurchaseDetails;

    public Purchase() {

    }

    @JsonCreator
    public Purchase(@JsonProperty("id") Long purchaseId,
                    @JsonProperty("supplier") Supplier supplier,
                    @JsonProperty("date") OffsetDateTime date,
                    @JsonProperty("status") statusShopping status,
                    @JsonProperty("total") BigDecimal total,
                    @JsonProperty("createdAt") OffsetDateTime createdAt,
                    @JsonProperty("updatedAt") OffsetDateTime updatedAt,
                    @JsonProperty("user") User user,
                    @JsonProperty("shoppingDetails") List<PurchaseDetails> purchaseDetails) {
        this.purchaseId = purchaseId;
        this.supplier = supplier;
        this.date = date;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
        this.PurchaseDetails = purchaseDetails;
    }

    public List<PurchaseDetails> getShoppingDetails() {
        return PurchaseDetails;
    }

    public void setShoppingDetails(List<PurchaseDetails> purchaseDetails) {
        PurchaseDetails = purchaseDetails;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public statusShopping getStatus() {
        return status;
    }

    public void setStatus(statusShopping status) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PurchaseDetails> getPurchaseDetails() {
        return PurchaseDetails;
    }

    public void setPurchaseDetails(List<PurchaseDetails> purchaseDetails) {
        PurchaseDetails = purchaseDetails;
    }

    public enum statusShopping {
        DRAFT, REGISTERED, ANNULLED;
    }

}
