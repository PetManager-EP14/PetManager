package com.ep14.pet_manager.service.impl;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ep14.pet_manager.DTO.UserSummary;
import com.ep14.pet_manager.entity.Permission;
import com.ep14.pet_manager.entity.UserPermission;
import com.ep14.pet_manager.entity.user;
import com.ep14.pet_manager.repository.PermissionRepository;
import com.ep14.pet_manager.repository.RoleRepository;
import com.ep14.pet_manager.repository.UserPermissionRepository;
import com.ep14.pet_manager.repository.UserRepository;
import com.ep14.pet_manager.service.UserAdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PermissionRepository permRepo;
    private final UserPermissionRepository userPermRepo;

    @Override
    @Transactional(readOnly = true)
    public UserSummary getUserSummary(String userId) {
        user u = userRepo.findById(UUID.fromString(userId))
            .orElseThrow(() -> new NoSuchElementException("User not found"));

        // permisos del rol
        Set<String> rolePerms = u.getRole().getPermissions()
            .stream().map(Permission::getCode).collect(Collectors.toSet());
        // permisos directos
        Set<String> direct = userPermRepo.findDirectCodesByUser(u.getUser_id());
        rolePerms.addAll(direct);

        return new UserSummary(
            u.getUser_id().toString(),
            u.getName(),
            u.getEmail(),
            u.getRole().getCode(),
            rolePerms
        );
    }

    @Override
    @Transactional
    public void assignRole(String userId, Long roleId) {
        var u = userRepo.findById(UUID.fromString(userId)).orElseThrow();
        var r = roleRepo.findById(roleId).orElseThrow();
        u.setRole(r);
        userRepo.save(u);
    }

    @Override
    @Transactional
    public void assignDirectPermissions(String userId, Set<String> codes) {
        var u = userRepo.findById(UUID.fromString(userId)).orElseThrow();

        // limpiar actuales
        userPermRepo.deleteByUser_User_id(u.getUser_id());

        // insertar nuevos
        for (String code : codes) {
            var p = permRepo.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("Unknown permission: " + code));
            var up = new UserPermission();
            up.setUser(u);
            up.setPermission(p);
            up.setId(new UserPermission.Id(u.getUser_id(), p.getPermissionId()));
            userPermRepo.save(up);
        }
    }
}