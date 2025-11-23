package com.ep14.pet_manager.controller;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.EntityModel;

import com.ep14.pet_manager.assembler.UserSummaryModelAssembler;
import com.ep14.pet_manager.dto.AssignPermissionsRequest;
import com.ep14.pet_manager.dto.AssignRoleRequest;
import com.ep14.pet_manager.dto.UserSummary;
import com.ep14.pet_manager.service.UserAdminService;

class UserAdminControllerTest {

    @Mock
    private UserAdminService service;

    @Mock
    private UserSummaryModelAssembler assembler;

    private UserAdminController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new UserAdminController(service, assembler);
    }

    // 1. get(): devuelve el resumen de usuario obtenido del servicio
    @Test
    void get_shouldReturnUserSummary_whenServiceReturnsData() {
        UserSummary expected = new UserSummary("123", "John", "john@example.com", "ADMIN", Set.of("user.read"));

        when(service.getUserSummary("123")).thenReturn(expected);

        when(assembler.toModel(any(UserSummary.class)))
            .thenReturn(EntityModel.of(expected));

        EntityModel<UserSummary> result = controller.get("123");

        assertThat(result.getContent()).isEqualTo(expected);
        verify(service).getUserSummary("123");
    }

    // 2. assignRole(): llama al servicio con el id y el roleId correctos
    @Test
    void assignRole_shouldCallServiceWithCorrectParams() {
        String userId = "u-001";
        AssignRoleRequest req = new AssignRoleRequest(10L);

        controller.assignRole(userId, req);

        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> roleCaptor = ArgumentCaptor.forClass(Long.class);

        verify(service).assignRole(idCaptor.capture(), roleCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(userId);
        assertThat(roleCaptor.getValue()).isEqualTo(10L);
    }

    // 3. assignPermissions(): llama al servicio con los permisos correctos
    @Test
    void assignPermissions_shouldCallServiceWithCorrectParams() {
        String userId = "u-002";
        Set<String> perms = Set.of("perm.a", "perm.b");
        AssignPermissionsRequest req = new AssignPermissionsRequest(perms);

        controller.assignPermissions(userId, req);

        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Set<String>> permsCaptor = ArgumentCaptor.forClass(Set.class);

        verify(service).assignDirectPermissions(idCaptor.capture(), permsCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(userId);
        assertThat(permsCaptor.getValue()).containsExactlyElementsOf(perms);
    }
}
