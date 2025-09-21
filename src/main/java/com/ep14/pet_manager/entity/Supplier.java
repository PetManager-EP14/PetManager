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
    private Long supplier_id;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    private String nit;

    @Column
    private String phone;

    @Column(nullable = false)
    private OffsetDateTime created_at = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime updated_at = OffsetDateTime.now();

    @OneToMany(mappedBy = "supplier")
    private List<Shopping> shopping;

    @OneToMany(mappedBy = "supplier")
    private List<SupplierProducts> supplier_product;

    public Supplier() {
    }

    @JsonCreator
    public Supplier(
            @JsonProperty("supplier_id") Long supplier_id,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("nit") String nit,
            @JsonProperty("phone") String phone,
            @JsonProperty("created_at") OffsetDateTime created_at,
            @JsonProperty("updated_at") OffsetDateTime updated_at,
            @JsonProperty("shopping") List<Shopping> shopping,
            @JsonProperty("supplier_product") List<SupplierProducts> supplier_product) {
        this.supplier_id = supplier_id;
        this.name = name;
        this.email = email;
        this.nit = nit;
        this.phone = phone;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.shopping = shopping;
        this.supplier_product = supplier_product;
    }

    public Long getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(Long supplier_id) {
        this.supplier_id = supplier_id;
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

    public List<Shopping> getShopping() {
        return shopping;
    }

    public void setShopping(List<Shopping> shopping) {
        this.shopping = shopping;
    }

    public List<SupplierProducts> getSupplier_product() {
        return supplier_product;
    }

    public void setSupplier_product(List<SupplierProducts> supplier_product) {
        this.supplier_product = supplier_product;
    }

}
