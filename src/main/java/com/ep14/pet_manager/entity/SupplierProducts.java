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

    @Column(precision = 12, scale = 2)
    private BigDecimal cost_ref;

    @Column
    private Integer lead_time_days;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    @Column(nullable = false)
    private OffsetDateTime created_at = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime updated_at = OffsetDateTime.now();

    public SupplierProducts() {
    }

    @JsonCreator
    public SupplierProducts(@JsonProperty("product") Product product,
                            @JsonProperty("supplier") Supplier supplier,
                            @JsonProperty("cost_ref") BigDecimal cost_ref,
                            @JsonProperty("lead_time_days") Integer lead_time_days,
                            @JsonProperty("active") boolean active,
                            @JsonProperty("created_id") OffsetDateTime created_at,
                            @JsonProperty("updated_at") OffsetDateTime updated_at) {
        this.product = product;
        this.supplier = supplier;
        this.cost_ref = cost_ref;
        this.lead_time_days = lead_time_days;
        this.active = active;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public BigDecimal getCost_ref() {
        return cost_ref;
    }

    public void setCost_ref(BigDecimal cost_ref) {
        this.cost_ref = cost_ref;
    }

    public Integer getLead_time_days() {
        return lead_time_days;
    }

    public void setLead_time_days(Integer lead_time_days) {
        this.lead_time_days = lead_time_days;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

}
