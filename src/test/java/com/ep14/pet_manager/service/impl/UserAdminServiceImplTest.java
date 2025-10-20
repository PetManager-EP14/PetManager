package com.ep14.pet_manager.service.impl;

import com.ep14.pet_manager.dto.UserSummary;
import com.ep14.pet_manager.entity.Permission;
import com.ep14.pet_manager.entity.Role;
import com.ep14.pet_manager.entity.User;
import com.ep14.pet_manager.entity.UserPermission;
import com.ep14.pet_manager.repository.PermissionRepository;
import com.ep14.pet_manager.repository.RoleRepository;
import com.ep14.pet_manager.repository.UserPermissionRepository;
import com.ep14.pet_manager.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserAdminServiceImplTest {

    @Mock UserRepository userRepo;
    @Mock RoleRepository roleRepo;
    @Mock PermissionRepository permRepo;
    @Mock UserPermissionRepository userPermRepo;

    @InjectMocks UserAdminServiceImpl service;

    private UUID userId;
    private User user;
    private Role role;
    private Permission perm;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        user = new User();
        user.setUserId(userId);
        user.setName("Ana");
        user.setEmail("ana@mail.com");

        role = new Role();
        role.setRoleId(10L);
        role.setCode("ADMIN");

        perm = new Permission();
        perm.setPermissionId(1L);
        perm.setCode("P_READ");
    }

    // ---------- getUserSummary ----------

    @Test
    void getUserSummary_withRoleAndDirectPerms_returnsExpectedSummary() {
        // mock datos
        role.setPermissions(Set.of(perm));
        user.setRole(role);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(userPermRepo.findDirectCodesByUser(userId)).thenReturn(Set.of("P_WRITE"));

        // ejecutar
        UserSummary summary = service.getUserSummary(userId.toString());

        // verificar
        assertThat(summary.name()).isEqualTo("Ana");
        assertThat(summary.email()).isEqualTo("ana@mail.com");
        assertThat(summary.roleCode()).isEqualTo("ADMIN");
        assertThat(summary.effectivePermissions()).contains("P_READ", "P_WRITE");
    }

    @Test
    void getUserSummary_withoutRole_handlesNullRoleSafely() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(userPermRepo.findDirectCodesByUser(userId)).thenReturn(Set.of());

        UserSummary summary = service.getUserSummary(userId.toString());

        assertThat(summary.roleCode()).isNull();
        assertThat(summary.effectivePermissions()).isEmpty();
    }

    @Test
    void getUserSummary_whenUserNotFound_throwsException() {
        when(userRepo.findById(any())).thenReturn(Optional.empty());

        String idString = userId.toString();

        assertThatThrownBy(() -> service.getUserSummary(idString))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("User not found");
    }

    // ---------- assignRole ----------

    @Test
    void assignRole_assignsAndSavesUser() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepo.findById(10L)).thenReturn(Optional.of(role));

        service.assignRole(userId.toString(), 10L);

        verify(userRepo).save(user);
        assertThat(user.getRole()).isEqualTo(role);
    }

    @Test
    void assignRole_whenRoleNotFound_throwsException() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepo.findById(10L)).thenReturn(Optional.empty());

        String idString = userId.toString();

        assertThatThrownBy(() -> service.assignRole(idString, 10L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Role not found");
    }

    // ---------- assignDirectPermissions ----------

    @Test
    void assignDirectPermissions_withValidCodes_savesEachPermission() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(permRepo.findByCode("P_READ")).thenReturn(Optional.of(perm));
        Permission pWrite = new Permission();
        pWrite.setPermissionId(2L);
        pWrite.setCode("P_WRITE");
        when(permRepo.findByCode("P_WRITE")).thenReturn(Optional.of(pWrite));

        service.assignDirectPermissions(userId.toString(), Set.of("P_READ", "P_WRITE"));

        verify(userPermRepo).deleteByIdUserId(userId);
        verify(userPermRepo, times(2)).save(any(UserPermission.class));
    }

    @Test
    void assignDirectPermissions_withUnknownCode_throwsException() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(permRepo.findByCode("X_INVALID")).thenReturn(Optional.empty());

        String idString = userId.toString();

        assertThatThrownBy(() -> service.assignDirectPermissions(idString, Set.of("X_INVALID")))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Unknown permission");
    }

    // ---------- parseUuid ----------

    @Test
    void parseUuid_invalidFormat_throwsException() {
        assertThatThrownBy(() -> service.assignRole("NO-UUID", 10L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Invalid userId");
    }
}
