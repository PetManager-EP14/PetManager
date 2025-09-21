package com.ep14.pet_manager.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "shopping_details")
public class PurchaseDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopping_detail_id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Purchase shopping;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal amount;

    @Column(nullable = false)
    private OffsetDateTime created_at = OffsetDateTime.now();

    public PurchaseDetails() {
    }

    @JsonCreator
    public PurchaseDetails(@JsonProperty("shopping_detail_id") Long shopping_detail_id,
                           @JsonProperty("shopping") Purchase shopping,
                           @JsonProperty("product") Product product,
                           @JsonProperty("amount") BigDecimal amount,
                           @JsonProperty("created_at") OffsetDateTime created_at) {
        this.shopping_detail_id = shopping_detail_id;
        this.shopping = shopping;
        this.product = product;
        this.amount = amount;
        this.created_at = created_at;
    }

    public Long getShopping_detail_id() {
        return shopping_detail_id;
    }

    public void setShopping_detail_id(Long shopping_detail_id) {
        this.shopping_detail_id = shopping_detail_id;
    }

    public Purchase getShopping() {
        return shopping;
    }

    public void setShopping(Purchase shopping) {
        this.shopping = shopping;
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

    public OffsetDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(OffsetDateTime created_at) {
        this.created_at = created_at;
    }
}
