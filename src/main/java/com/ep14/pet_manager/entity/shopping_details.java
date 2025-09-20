package com.ep14.pet_manager.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "shopping_details")
public class shopping_details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopping_detail_id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private shopping shopping;

    @ManyToOne
    @JoinColumn(nullable = false)
    private product product;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal amount;

    @Column(nullable = false)
    private OffsetDateTime created_at = OffsetDateTime.now();

    public shopping_details() {
    }

    @JsonCreator
    public shopping_details(@JsonProperty("shopping_detail_id") Long shopping_detail_id,
            @JsonProperty("shopping") com.ep14.pet_manager.entity.shopping shopping,
            @JsonProperty("product") com.ep14.pet_manager.entity.product product,
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

    public shopping getShopping() {
        return shopping;
    }

    public void setShopping(shopping shopping) {
        this.shopping = shopping;
    }

    public product getProduct() {
        return product;
    }

    public void setProduct(product product) {
        this.product = product;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OffsetDateTime getCreated() {
        return created_at;
    }

    public void setCreated(OffsetDateTime created_at) {
        this.created_at = created_at;
    }

}
