package com.ep14.pet_manager.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class SaleDTO {

    private Long saleId;
    private UUID userId;
    private OffsetDateTime date;
    private String method;
    private String status;
    private BigDecimal total;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<SaleDetailDTO> details;

    // Constructor vacío
    public SaleDTO() {
    }

    // Constructor completo
    public SaleDTO(Long saleId, UUID userId, OffsetDateTime date, String method, String status,
                   BigDecimal total, OffsetDateTime createdAt, OffsetDateTime updatedAt,
                   List<SaleDetailDTO> details) {
        this.saleId = saleId;
        this.userId = userId;
        this.date = date;
        this.method = method;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.details = details;
    }

    // Getters y Setters
    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public List<SaleDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<SaleDetailDTO> details) {
        this.details = details;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long saleId;
        private UUID userId;
        private OffsetDateTime date;
        private String method;
        private String status;
        private BigDecimal total;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private List<SaleDetailDTO> details;

        public Builder saleId(Long saleId) {
            this.saleId = saleId;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder date(OffsetDateTime date) {
            this.date = date;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder status(String status) {
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

        public Builder details(List<SaleDetailDTO> details) {
            this.details = details;
            return this;
        }

        public SaleDTO build() {
            return new SaleDTO(saleId, userId, date, method, status, total, createdAt, updatedAt, details);
        }
    }
}
