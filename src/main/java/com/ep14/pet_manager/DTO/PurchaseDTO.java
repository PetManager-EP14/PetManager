package com.ep14.pet_manager.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class PurchaseDTO implements Serializable {

    private Long id;
    private Long supplierId;
    private OffsetDateTime date;
    private StatusShopping status;
    private BigDecimal total;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Long userId;
    private List<Long> shoppingDetailIds;

    public enum StatusShopping {
        DRAFT, REGISTERED, ANNULLED
    }

    public PurchaseDTO() {
    }

    public PurchaseDTO(Long id, Long supplierId, OffsetDateTime date, StatusShopping status, BigDecimal total,
                       OffsetDateTime createdAt,
                       OffsetDateTime updatedAt,
                       Long userId,
                       List<Long> shoppingDetailIds
    ) {
        this.id = id;
        this.supplierId = supplierId;
        this.date = date;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.shoppingDetailIds = shoppingDetailIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public StatusShopping getStatus() {
        return status;
    }

    public void setStatus(StatusShopping status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getShoppingDetailIds() {
        return shoppingDetailIds;
    }

    public void setShoppingDetailIds(List<Long> shoppingDetailIds) {
        this.shoppingDetailIds = shoppingDetailIds;
    }
}
