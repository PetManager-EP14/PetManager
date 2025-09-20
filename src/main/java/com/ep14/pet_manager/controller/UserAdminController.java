package com.ep14.pet_manager.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ep14.pet_manager.DTO.AssignPermissionsRequest;
import com.ep14.pet_manager.DTO.AssignRoleRequest;
import com.ep14.pet_manager.DTO.UserSummary;
import com.ep14.pet_manager.service.UserAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService service;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user.read')")
    public UserSummary get(@PathVariable String id) {
        return service.getUserSummary(id);
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