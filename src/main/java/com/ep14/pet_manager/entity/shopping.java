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
public class shopping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopping_id;

    @ManyToOne
    @JoinColumn(name = "shoppings", nullable = false)
    private suppliers suppliers;

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
    @JoinColumn(name = "shoppings", nullable = false)
    private user user;

    @OneToMany(mappedBy = "shopping")
    private List<shopping_details> shopping_details ;

    public shopping(){

    }

    @JsonCreator
    public shopping(@JsonProperty("id") Long shopping_id,
                @JsonProperty("suppliers") com.ep14.pet_manager.entity.suppliers suppliers,
                @JsonProperty("date") OffsetDateTime date,
                @JsonProperty("status") status_shopping status,
                @JsonProperty("total") BigDecimal total,
                @JsonProperty("created_at") OffsetDateTime created_at,
                @JsonProperty("updated_at") OffsetDateTime updated_at,
                @JsonProperty("user") com.ep14.pet_manager.entity.user user,
                @JsonProperty("shopping_details") List<shopping_details> shopping_details ) {
        this.shopping_id = shopping_id;
        this.suppliers = suppliers;
        this.date = date;
        this.status = status;
        this.total = total;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user = user;
        this.shopping_details = shopping_details;
    }

    public Long getShopping_id() {
        return shopping_id;
    }

    public void setShopping_id(Long shopping_id) {
        this.shopping_id = shopping_id;
    }

    public suppliers getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(suppliers suppliers) {
        this.suppliers = suppliers;
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

    public user getUser() {
        return user;
    }

    public void setUser(user user) {
        this.user = user;
    }

    public List<shopping_details> getShopping_details() {
        return shopping_details;
    }

    public void setShopping_details(List<shopping_details> shopping_details) {
        this.shopping_details = shopping_details;
    }
    
    public enum status_shopping {
        DRAFT, REGISTERED, ANNULLED;
    }

}
