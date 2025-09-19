package com.ep14.pet_manager.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "supplier_products")
public class supplier_products {
    
    @ManyToOne
    @JoinColumn(name = "product")
    private product product;
    
    @ManyToOne
    @JoinColumn(name = "supplier_product")
    private suppliers suppliers;

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

    public supplier_products() {
    }

    public supplier_products(com.ep14.pet_manager.entity.product product,
            com.ep14.pet_manager.entity.suppliers suppliers, BigDecimal cost_ref, Integer lead_time_days,
            boolean active, OffsetDateTime created_at, OffsetDateTime updated_at) {
        this.product = product;
        this.suppliers = suppliers;
        this.cost_ref = cost_ref;
        this.lead_time_days = lead_time_days;
        this.active = active;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public product getProduct() {
        return product;
    }

    public void setProduct(product product) {
        this.product = product;
    }

    public suppliers getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(suppliers suppliers) {
        this.suppliers = suppliers;
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
