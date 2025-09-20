package com.ep14.pet_manager.entity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class user {
    @Id
    @GeneratedValue
    @Column(nullable = false, columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID user_id;

    @Column(nullable = false)
    private Long role_id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String password_hash;

    @Column(nullable = false)
    private OffsetDateTime created_at = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime updated_at = OffsetDateTime.now();

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private role role;

    @OneToMany(mappedBy = "user")
    private List<sale> sales;

    @OneToMany(mappedBy = "shopping")
    private List<shopping> shoppings;

    public user() {
    }

    @JsonCreator
    public user(@JsonProperty("user_id") UUID user_id,
            @JsonProperty("role_id") Long role_id,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("phone") String phone,
            @JsonProperty("address") String address,
            @JsonProperty("password_hash") String password_hash,
            @JsonProperty("created_at") OffsetDateTime created_at,
            @JsonProperty("updated_at") OffsetDateTime updated_at,
            @JsonProperty("role") com.ep14.pet_manager.entity.role role,
            @JsonProperty("sales") List<sale> sales,
            @JsonProperty("shoppings") List<shopping> shoppings) {
        this.user_id = user_id;
        this.role_id = role_id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password_hash = password_hash;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.role = role;
        this.sales = sales;
        this.shoppings = shoppings;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
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

    public role getRole() {
        return role;
    }

    public void setRole(role role) {
        this.role = role;
    }

    public List<sale> getSales() {
        return sales;
    }

    public void setSales(List<sale> sales) {
        this.sales = sales;
    }

    public List<shopping> getShoppings() {
        return shoppings;
    }

    public void setShoppings(List<shopping> shoppings) {
        this.shoppings = shoppings;
    }

}