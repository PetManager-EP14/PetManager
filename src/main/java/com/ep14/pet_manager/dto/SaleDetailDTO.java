package com.ep14.pet_manager.dto;

import java.math.BigDecimal;

public class SaleDetailDTO {
    private Long saleDetailId;
    private Long productId;
    private BigDecimal amount;

    // Getters & Setters
    public Long getSaleDetailId() {
        return saleDetailId;
    }

    public void setSaleDetailId(Long saleDetailId) {
        this.saleDetailId = saleDetailId;
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
}
