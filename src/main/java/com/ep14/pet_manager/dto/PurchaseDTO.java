package com.ep14.pet_manager.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class PurchaseDTO implements Serializable {
    private Long id;
    private Long supplierId;
    private OffsetDateTime date;
    private StatusShopping status;
    private BigDecimal total;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private UUID userId;
    // Campo actual que solo contiene los IDs de los detalles de la compra (shopping_details)
    private List<Long> shoppingDetailIds; 

    // Definición del estado de la compra, reflejando el enum de la entidad Purchase (statusShopping)
    public enum StatusShopping {
        DRAFT, REGISTERED, ANNULLED
    }

    public PurchaseDTO() {
    }

    // Constructor privado para el patrón Builder
    private PurchaseDTO(Builder builder) {
        this.id = builder.id; 
        this.supplierId = builder.supplierId; 
        this.date = builder.date; 
        this.status = builder.status; 
        this.total = builder.total; 
        this.createdAt = builder.createdAt;  
        this.updatedAt = builder.updatedAt; 
        this.userId = builder.userId; 
        this.shoppingDetailIds = builder.shoppingDetailIds; 
    }

    // Clase Builder para construir inmutables (utilizada en PurchaseDTOTest [12])
    public static class Builder {
        private Long id; 
        private Long supplierId; 
        private OffsetDateTime date; 
        private StatusShopping status; 
        private BigDecimal total; 
        private OffsetDateTime createdAt; 
        private OffsetDateTime updatedAt; 
        private UUID userId; 
        private List<Long> shoppingDetailIds; 

        public Builder id(Long id) { 
            this.id = id;
            return this;
        }
        public Builder supplierId(Long supplierId) { 
            this.supplierId = supplierId;
            return this;
        }
        public Builder date(OffsetDateTime date) { 
            this.date = date;
            return this;
        }
        public Builder status(StatusShopping status) { 
            this.status = status;
            return this;
        }
        public Builder total(BigDecimal total) {
            this.total = total;
            return this;
        }
        public Builder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Builder updatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        public Builder userId(UUID userId) {    
            this.userId = userId;
            return this;
        }
        public Builder shoppingDetailIds(List<Long> shoppingDetailIds) { 
            this.shoppingDetailIds = shoppingDetailIds;
            return this;
        }
        public PurchaseDTO build() { 
            return new PurchaseDTO(this);
        }
    }

    // Getters y Setters
    public Long getId() { return id; } 
    public void setId(Long id) { this.id = id; } 
    public Long getSupplierId() { return supplierId; } 
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; } 
    public OffsetDateTime getDate() { return date; } 
    public void setDate(OffsetDateTime date) { this.date = date; } 
    public StatusShopping getStatus() { return status; } 
    public void setStatus(StatusShopping status) { this.status = status; } 
    public BigDecimal getTotal() { return total; } 
    public void setTotal(BigDecimal total) { this.total = total; } 
    public OffsetDateTime getCreatedAt() { return createdAt; } 
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; } 
    public OffsetDateTime getUpdatedAt() { return updatedAt; } 
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; } 
    public UUID getUserId() { return userId; } 
    public void setUserId(UUID userId) { this.userId = userId; } 
    public List<Long> getShoppingDetailIds() { return shoppingDetailIds; } 
    public void setShoppingDetailIds(List<Long> shoppingDetailIds) { this.shoppingDetailIds = shoppingDetailIds; } 


}