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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false)
    private String name;

    @Column
    private String category;

    @Column(nullable = false, precision = 12, scale = 3, columnDefinition = "numeric(12,3) default 0 check (stock >= 0)")
    private BigDecimal stock = BigDecimal.ZERO;

    @Column(name = "price_shopping", precision = 12, scale = 2)
    private BigDecimal priceShopping;

    @Column(name = "price_sale", precision = 12, scale = 2)
    private BigDecimal priceSale;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @OneToMany(mappedBy = "product")
    private List<SaleDetails> saleDetails;

    @OneToMany(mappedBy = "product")
    private List<SupplierProducts> supplierProducts;

    @OneToMany(mappedBy = "product")
    private List<PurchaseDetails> purchaseDetails;

    public Product() {

    }

    @JsonCreator
    public Product(@JsonProperty("productId") Long productId,
                   @JsonProperty("name") String name,
                   @JsonProperty("category") String category,
                   @JsonProperty("stock") BigDecimal stock,
                   @JsonProperty("priceShopping") BigDecimal priceShopping,
                   @JsonProperty("priceSale") BigDecimal priceSale,
                   @JsonProperty("createdAt") OffsetDateTime createdAt,
                   @JsonProperty("updatedAt") OffsetDateTime updatedAt,
                   @JsonProperty("saleDetails") List<SaleDetails> saleDetails,
                   @JsonProperty("supplierProduct") List<SupplierProducts> SupplierProducts,
                   @JsonProperty("shoppingDetails") List<PurchaseDetails> PurchaseDetails) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.priceShopping = priceShopping;
        this.priceSale = priceSale;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.saleDetails = saleDetails;
        this.supplierProducts = SupplierProducts;
        this.purchaseDetails = PurchaseDetails;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public BigDecimal getPriceShopping() {
        return priceShopping;
    }

    public void setPriceShopping(BigDecimal priceShopping) {
        this.priceShopping = priceShopping;
    }

    public BigDecimal getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(BigDecimal priceSale) {
        this.priceSale = priceSale;
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

    public List<SaleDetails> getSaleDetails() {
        return saleDetails;
    }

    public void setSaleDetails(List<SaleDetails> saleDetails) {
        this.saleDetails = saleDetails;
    }

    public List<SupplierProducts> getSupplierProducts() {
        return supplierProducts;
    }

    public void setSupplierProducts(List<SupplierProducts> supplierProducts) {
        this.supplierProducts = supplierProducts;
    }

    public List<PurchaseDetails> getPurchaseDetails() {
        return purchaseDetails;
    }

    public void setPurchaseDetails(List<PurchaseDetails> purchaseDetails) {
        this.purchaseDetails = purchaseDetails;
    }

}
