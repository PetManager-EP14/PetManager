package com.ep14.pet_manager.dto;

import java.math.BigDecimal;

/**
 * DTO para representar el detalle de un producto dentro de una compra.
 * Necesario para enviar información anidada de producto, cantidad y precio.
 */
public class PurchaseDetailDTO {

    // ID del detalle de la compra (mapeado desde PurchaseDetails.purchaseDetailId)
    private Long purchaseDetailId;

    // ID del producto (para re-crear la relación en el mapper)
    private Long productId;

    // Cantidad comprada (PurchaseDetails.amount)
    private BigDecimal amount;

    // Nombre del producto (mapeado desde Product.name por el mapper)
    private String productName;

    // Precio unitario de compra (mapeado desde Product.priceShopping por el mapper)
    private BigDecimal unitPrice;

    // Constructores (vacío y completo si se requiere)
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