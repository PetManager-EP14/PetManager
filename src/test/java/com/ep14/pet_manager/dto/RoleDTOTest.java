package com.ep14.pet_manager.dto;

import com.ep14.pet_manager.entity.User;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoleDTOTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        RoleDTO dto = new RoleDTO();
        OffsetDateTime now = OffsetDateTime.now();
        User user = new User();

        dto.setRoleId(1L);
        dto.setCode("ADMIN");
        dto.setDescription("Administrador general");
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        dto.setUsers(List.of(user));

        assertThat(dto.getRoleId()).isEqualTo(1L);
        assertThat(dto.getCode()).isEqualTo("ADMIN");
        assertThat(dto.getDescription()).isEqualTo("Administrador general");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
        assertThat(dto.getUsers()).containsExactly(user);
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        OffsetDateTime created = OffsetDateTime.now();
        OffsetDateTime updated = created.plusDays(1);
        User user1 = new User();
        user1.setName("Juan");

        RoleDTO dto = new RoleDTO(
                99L,
                "USER",
                "Usuario estándar",
                created,
                updated,
                List.of(user1)
        );

        assertThat(dto.getRoleId()).isEqualTo(99L);
        assertThat(dto.getCode()).isEqualTo("USER");
        assertThat(dto.getDescription()).isEqualTo("Usuario estándar");
        assertThat(dto.getCreatedAt()).isEqualTo(created);
        assertThat(dto.getUpdatedAt()).isEqualTo(updated);
        assertThat(dto.getUsers().get(0).getName()).isEqualTo("Juan");
    }
}
