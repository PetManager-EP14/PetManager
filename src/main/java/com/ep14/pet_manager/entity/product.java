package com.ep14.pet_manager.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    @Column(nullable = false)
    private String name;

    @Column
    private String category;

    @Column(nullable = false, precision = 12, scale = 3, columnDefinition = "numeric(12,3) default 0 check (stock >= 0)")
    private BigDecimal stock = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal price_shopping;

    @Column(precision = 12, scale = 2)
    private BigDecimal price_sale;

    @Column(nullable = false)
    private OffsetDateTime created_at = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime updated_at = OffsetDateTime.now();

    @OneToMany(mappedBy = "product")
    private List<sale_details> sale_details;

    @OneToMany(mappedBy = "product")
    private List<supplier_products> supplier_products;

    @OneToMany(mappedBy = "product")
    private List<shopping_details> shopping_details;

    public product() {

    }

    @JsonCreator
    public product(@JsonProperty("product_id") Long product_id,
            @JsonProperty("name") String name,
            @JsonProperty("category") String category,
            @JsonProperty("stock") BigDecimal stock,
            @JsonProperty("price_shopping") BigDecimal price_shopping,
            @JsonProperty("price_sale") BigDecimal price_sale,
            @JsonProperty("created_at") OffsetDateTime created_at,
            @JsonProperty("updated_at") OffsetDateTime updated_at,
            @JsonProperty("sale_details") List<sale_details> sale_details,
            @JsonProperty("supplier_product") List<supplier_products> supplier_products,
            @JsonProperty("shopping_details") List<shopping_details> shopping_details) {
        this.product_id = product_id;
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.price_shopping = price_shopping;
        this.price_sale = price_sale;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.sale_details = sale_details;
        this.supplier_products = supplier_products;
        this.shopping_details = shopping_details;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice_shopping() {
        return price_shopping;
    }

    public void setPrice_shopping(BigDecimal price_shopping) {
        this.price_shopping = price_shopping;
    }

    public BigDecimal getPrice_sale() {
        return price_sale;
    }

    public void setPrice_sale(BigDecimal price_sale) {
        this.price_sale = price_sale;
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

    public List<sale_details> getSale_details() {
        return sale_details;
    }

    public void setSale_details(List<sale_details> sale_details) {
        this.sale_details = sale_details;
    }

    public List<supplier_products> getSupplier_products() {
        return supplier_products;
    }

    public void setSupplier_products(List<supplier_products> supplier_products) {
        this.supplier_products = supplier_products;
    }

    public List<shopping_details> getShopping_details() {
        return shopping_details;
    }

    public void setShopping_details(List<shopping_details> shopping_details) {
        this.shopping_details = shopping_details;
    }

}
