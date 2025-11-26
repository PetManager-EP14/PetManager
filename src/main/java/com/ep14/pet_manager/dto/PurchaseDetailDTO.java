package com.ep14.pet_manager.dto;

import java.math.BigDecimal;
import java.io.Serializable;
// Se recomienda el uso de camelCase para variables y clases [3-5].

public class PurchaseDetailDTO implements Serializable {

    private Long purchaseDetailId;
    private Long productId;
    // Cantidad comprada
    private BigDecimal amount;
    // Nombre del producto (mapeado desde Product.name)
    private String productName;
    // Precio unitario de compra (mapeado desde Product.priceShopping)
    private BigDecimal unitPrice;

    public PurchaseDetailDTO() {
    }

    // Getters y Setters
    public Long getPurchaseDetailId() {
        return purchaseDetailId;
    }

    public void setPurchaseDetailId(Long purchaseDetailId) {
        this.purchaseDetailId = purchaseDetailId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
