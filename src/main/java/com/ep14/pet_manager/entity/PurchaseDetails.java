package com.ep14.pet_manager.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "shopping_details")
public class PurchaseDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_detail_id")
    private Long purchaseDetailId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public PurchaseDetails() {
    }

    @JsonCreator
    public PurchaseDetails(@JsonProperty("shoppingDetailId") Long purchaseDetailId,
                           @JsonProperty("shopping") Purchase purchase,
                           @JsonProperty("product") Product product,
                           @JsonProperty("amount") BigDecimal amount,
                           @JsonProperty("createdAt") OffsetDateTime createdAt) {
        this.purchaseDetailId = purchaseDetailId;
        this.purchase = purchase;
        this.product = product;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public Long getPurchaseDetailId() {
        return purchaseDetailId;
    }

    public void setPurchaseDetailId(Long purchaseDetailId) {
        this.purchaseDetailId = purchaseDetailId;
    }

    public Purchase getShopping() {
        return purchase;
    }

    public void setShopping(Purchase purchase) {
        this.purchase = purchase;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
