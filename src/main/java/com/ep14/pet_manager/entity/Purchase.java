package com.ep14.pet_manager.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "shoppings")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopping_id;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private OffsetDateTime date = OffsetDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private status_shopping status = status_shopping.DRAFT;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(nullable = false)
    private OffsetDateTime created_at = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime updated_at = OffsetDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "purchase")
    private List<PurchaseDetails> PurchaseDetails;

    public Purchase() {

    }

    @JsonCreator
    public Purchase(@JsonProperty("id") Long shopping_id,
                    @JsonProperty("supplier") Supplier supplier,
                    @JsonProperty("date") OffsetDateTime date,
                    @JsonProperty("status") status_shopping status,
                    @JsonProperty("total") BigDecimal total,
                    @JsonProperty("created_at") OffsetDateTime created_at,
                    @JsonProperty("updated_at") OffsetDateTime updated_at,
                    @JsonProperty("user") User user,
                    @JsonProperty("shopping_details") List<PurchaseDetails> PurchaseDetails) {
        this.shopping_id = shopping_id;
        this.supplier = supplier;
        this.date = date;
        this.status = status;
        this.total = total;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user = user;
        this.PurchaseDetails = PurchaseDetails;
    }

    public List<PurchaseDetails> getShoppingDetails() {
        return PurchaseDetails;
    }

    public void setShoppingDetails(List<PurchaseDetails> purchaseDetails) {
        PurchaseDetails = purchaseDetails;
    }

    public Long getShopping_id() {
        return shopping_id;
    }

    public void setShopping_id(Long shopping_id) {
        this.shopping_id = shopping_id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public status_shopping getStatus() {
        return status;
    }

    public void setStatus(status_shopping status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PurchaseDetails> getShopping_details() {
        return PurchaseDetails;
    }

    public void setShopping_details(List<PurchaseDetails> PurchaseDetails) {
        this.PurchaseDetails = PurchaseDetails;
    }

    public enum status_shopping {
        DRAFT, REGISTERED, ANNULLED;
    }

}
