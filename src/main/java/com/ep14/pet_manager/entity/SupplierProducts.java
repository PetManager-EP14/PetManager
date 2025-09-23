package com.ep14.pet_manager.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "supplier_products")
public class SupplierProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "supplier")
    private Supplier supplier;

    @Column(name = "cost_ref", precision = 12, scale = 2)
    private BigDecimal costRef;

    @Column(name = "lead_time_days")
    private Integer leadTimeDays;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    public SupplierProducts() {
    }

    @JsonCreator
    public SupplierProducts(@JsonProperty("product") Product product,
                            @JsonProperty("supplier") Supplier supplier,
                            @JsonProperty("costRef") BigDecimal costRef,
                            @JsonProperty("leadTimeDays") Integer leadTimeDays,
                            @JsonProperty("active") boolean active,
                            @JsonProperty("createdAt") OffsetDateTime createdAt,
                            @JsonProperty("updatedAt") OffsetDateTime updatedAt) {
        this.product = product;
        this.supplier = supplier;
        this.costRef = costRef;
        this.leadTimeDays = leadTimeDays;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public BigDecimal getCostRef() {
        return costRef;
    }

    public void setCostRef(BigDecimal costRef) {
        this.costRef = costRef;
    }

    public Integer getLeadTimeDays() {
        return leadTimeDays;
    }

    public void setLeadTimeDays(Integer leadTimeDays) {
        this.leadTimeDays = leadTimeDays;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

}
