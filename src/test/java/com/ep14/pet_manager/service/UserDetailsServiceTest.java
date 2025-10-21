package com.ep14.pet_manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ep14.pet_manager.entity.Permission;
import com.ep14.pet_manager.entity.Role;
import com.ep14.pet_manager.entity.User;
import com.ep14.pet_manager.repository.UserPermissionRepository;
import com.ep14.pet_manager.repository.UserRepository;

class UsersDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPermissionRepository userPermRepo;

    @InjectMocks
    private UsersDetailsService service;

    private User mockUser;
    private UUID userId;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setEmail("user@example.com");
        mockUser.setPasswordHash("encrypted123");
    }

    // 1. Usuario con rol y permisos (flujo completo)
    @Test
    void loadUserByUsername_shouldReturnUserWithAuthorities() {
        Permission perm1 = new Permission();
        perm1.setCode("READ_DATA");

        Permission perm2 = new Permission();
        perm2.setCode("WRITE_LOGS");

        Role role = new Role();
        role.setCode("admin");
        role.setPermissions(Set.of(perm1, perm2));

        mockUser.setRole(role);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockUser));
        when(userPermRepo.findDirectCodesByUser(userId)).thenReturn(Set.of("DELETE_RECORDS"));

        UserDetails userDetails = service.loadUserByUsername("user@example.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("user@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("encrypted123");

        // Debe incluir los permisos y el rol en formato ROLE_ADMIN
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .contains("READ_DATA", "WRITE_LOGS", "DELETE_RECORDS", "ROLE_ADMIN");
    }

    // 2️. Usuario sin rol (solo permisos directos)
    @Test
    void loadUserByUsername_shouldReturnUserWithDirectPermissionsOnly() {
        mockUser.setRole(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(userPermRepo.findDirectCodesByUser(userId)).thenReturn(Set.of("VIEW_ONLY"));

        UserDetails userDetails = service.loadUserByUsername("user@example.com");

        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("VIEW_ONLY");
    }

    // 3️. Usuario no encontrado
    @Test
    void loadUserByUsername_shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("missing@example.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Usuario No Encontrado!");
    }

    // 4️. Rol con permisos nulos o vacíos
    @Test
    void loadUserByUsername_shouldHandleRoleWithNullPermissions() {
        Role role = new Role();
        role.setCode("employee");
        role.setPermissions(null);
        mockUser.setRole(role);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockUser));
        when(userPermRepo.findDirectCodesByUser(userId)).thenReturn(Set.of());

        UserDetails userDetails = service.loadUserByUsername("user@example.com");

        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .contains("ROLE_EMPLOYEE");
    }

    // 5. Permisos con espacios o nulos
    @Test
    void loadUserByUsername_shouldTrimAndIgnoreBlankPermissions() {
        Permission perm1 = new Permission();
        perm1.setCode("   READ_REPORTS  ");
        Permission perm2 = new Permission();
        perm2.setCode("   "); // Este debe ignorarse

        Role role = new Role();
        role.setCode("manager");
        role.setPermissions(Set.of(perm1, perm2));

        mockUser.setRole(role);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockUser));
        when(userPermRepo.findDirectCodesByUser(userId)).thenReturn(Set.of("EXPORT_DATA"));

        UserDetails userDetails = service.loadUserByUsername("user@example.com");

        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .contains("READ_REPORTS", "EXPORT_DATA", "ROLE_MANAGER");
    }
}
