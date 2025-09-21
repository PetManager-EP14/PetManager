package com.ep14.pet_manager.DTO;

import java.time.OffsetDateTime;
import java.util.List;

import com.ep14.pet_manager.entity.User;

public class RoleDTO {

    private Long role_id;
    private String code;
    private String description;
    private OffsetDateTime created_at;
    private OffsetDateTime updated_at;
    private List<User> Users;

    public RoleDTO(){

    }

    public RoleDTO(Long role_id, String code, String description, OffsetDateTime created_at, OffsetDateTime updated_at,
            List<User> Users) {
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
