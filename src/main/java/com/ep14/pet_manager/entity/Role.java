package com.ep14.pet_manager.entity;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(unique = true, length = 100)
    private String description;

    @Column(nullable = false)
    private OffsetDateTime created_at = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime updated_at = OffsetDateTime.now();

    @OneToMany(mappedBy = "role")
    private List<User> Users;

    public Role() {

    }

    @JsonCreator
    public Role(@JsonProperty("id") Long role_id,
                @JsonProperty("code") String code,
                @JsonProperty("description") String description,
                @JsonProperty("createdAt") OffsetDateTime created_at,
                @JsonProperty("updatedAt") OffsetDateTime updated_at,
                @JsonProperty("users") List<User> Users) {
        this.role_id = role_id;
        this.code = code;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.Users = Users;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<User> getUsers() {
        return Users;
    }

    public void setUsers(List<User> Users) {
        this.Users = Users;
    }

}