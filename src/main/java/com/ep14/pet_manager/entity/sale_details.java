package com.ep14.pet_manager.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sale_details")
public class sale_details {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sale_detail_id;

    @ManyToOne
    @JoinColumn(name = "sale_id", nullable = false)
    private sale sale;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private product product;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private OffsetDateTime created_at = OffsetDateTime.now();

    public sale_details() {
    }

    @JsonCreator
    public sale_details(@JsonProperty("sale_detail_id") Long sale_detail_id,
            @JsonProperty("sale") com.ep14.pet_manager.entity.sale sale,
            @JsonProperty("product") com.ep14.pet_manager.entity.product product,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("created_at") OffsetDateTime created_at) {
        this.sale_detail_id = sale_detail_id;
        this.sale = sale;
        this.product = product;
        this.amount = amount;
        this.created_at = created_at;
    }

    public Long getSale_detail_id() {
        return sale_detail_id;
    }

    public void setSale_detail_id(Long sale_detail_id) {
        this.sale_detail_id = sale_detail_id;
    }

    public sale getSale() {
        return sale;
    }

    public void setSale(sale sale) {
        this.sale = sale;
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

    public OffsetDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(OffsetDateTime created_at) {
        this.created_at = created_at;
    }

}
