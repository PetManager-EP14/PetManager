package com.ep14.pet_manager.entity;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    private String nit;

    @Column
    private String phone;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @OneToMany(mappedBy = "supplier")
    private List<Purchase> purchase;

    @OneToMany(mappedBy = "supplier")
    private List<SupplierProducts> supplierProduct;

    public Supplier() {
    }

    @JsonCreator
    public Supplier(
            @JsonProperty("supplierId") Long supplierId,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("nit") String nit,
            @JsonProperty("phone") String phone,
            @JsonProperty("createdAt") OffsetDateTime createdAt,
            @JsonProperty("updatedAt") OffsetDateTime updatedAt,
            @JsonProperty("shopping") List<Purchase> purchase,
            @JsonProperty("supplierProduct") List<SupplierProducts> supplierProduct) {
        this.supplierId = supplierId;
        this.name = name;
        this.email = email;
        this.nit = nit;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.purchase = purchase;
        this.supplierProduct = supplierProduct;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public List<Purchase> getShopping() {
        return purchase;
    }

    public void setShopping(List<Purchase> purchase) {
        this.purchase = purchase;
    }

    public List<SupplierProducts> getSupplierProduct() {
        return supplierProduct;
    }

    public void setSupplierProduct(List<SupplierProducts> supplierProduct) {
        this.supplierProduct = supplierProduct;
    }

    public List<Purchase> getPurchase() {
        return purchase;
    }

    public void setPurchase(List<Purchase> purchase) {
        this.purchase = purchase;
    }
}
