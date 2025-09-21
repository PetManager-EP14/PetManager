package com.ep14.pet_manager.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_permission")
public class UserPermission {

    @EmbeddedId
    private Id id = new Id();

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("permissionId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private Permission permission;

    // --- getters/setters ---
    public Id getId() { return id; }
    public void setId(Id id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Permission getPermission() { return permission; }
    public void setPermission(Permission permission) { this.permission = permission; }

    // ===== PK embebida =====
    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "user_id", columnDefinition = "uuid")
        private UUID userId;

        @Column(name = "permission_id")
        private Long permissionId;

        public Id() {}

        public Id(UUID userId, Long permissionId) {
            this.userId = userId;
            this.permissionId = permissionId;
        }

        public UUID getUserId() { return userId; }
        public void setUserId(UUID userId) { this.userId = userId; }

        public Long getPermissionId() { return permissionId; }
        public void setPermissionId(Long permissionId) { this.permissionId = permissionId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Id id1)) return false;
            return Objects.equals(userId, id1.userId) &&
                   Objects.equals(permissionId, id1.permissionId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, permissionId);
        }
    }
}