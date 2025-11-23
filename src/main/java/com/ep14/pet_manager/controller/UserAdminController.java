package com.ep14.pet_manager.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ep14.pet_manager.assembler.UserSummaryModelAssembler;
import com.ep14.pet_manager.dto.AssignPermissionsRequest;
import com.ep14.pet_manager.dto.AssignRoleRequest;
import com.ep14.pet_manager.dto.UserSummary;
import com.ep14.pet_manager.service.UserAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService service;
    private final UserSummaryModelAssembler assembler;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user.read')")
    public EntityModel<UserSummary> get(@PathVariable String id) {
        UserSummary userSummary = service.getUserSummary(id);
        return assembler.toModel(userSummary);
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasAuthority('user.assign_role')")
    public void assignRole(@PathVariable String id, @RequestBody AssignRoleRequest req) {
        service.assignRole(id, req.roleId());
    }

    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('user.assign_permissions')")
    public void assignPermissions(@PathVariable String id, @RequestBody AssignPermissionsRequest req) {
        service.assignDirectPermissions(id, req.permissions());
    }
}