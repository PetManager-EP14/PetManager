package com.ep14.pet_manager.service.Impl;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ep14.pet_manager.DTO.UserSummary;
import com.ep14.pet_manager.entity.Permission;
import com.ep14.pet_manager.entity.User;
import com.ep14.pet_manager.entity.UserPermission;
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
        UUID uuid = parseUuid(userId);

        User u = userRepo.findById(uuid)
            .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Role y permisos del rol (null-safe)
        var role = u.getRole();
        String roleCode = role != null ? role.getCode() : null;

        Set<String> rolePerms = role != null
            ? role.getPermissions().stream().map(Permission::getCode).collect(Collectors.toSet())
            : new HashSet<>();

        // Permisos directos
        Set<String> direct = userPermRepo.findDirectCodesByUser(u.getUser_id());
        rolePerms.addAll(direct);

        return new UserSummary(
            u.getUser_id() != null ? u.getUser_id().toString() : null,
            u.getName(),
            u.getEmail(),
            roleCode,
            rolePerms
        );
    }

    @Override
    @Transactional
    public void assignRole(String userId, Long roleId) {
        UUID uuid = parseUuid(userId);

        var u = userRepo.findById(uuid).orElseThrow(() -> new NoSuchElementException("User not found"));
        var r = roleRepo.findById(roleId).orElseThrow(() -> new NoSuchElementException("Role not found"));

        u.setRole(r);
        
        userRepo.save(u);
    }

    @Override
    @Transactional
    public void assignDirectPermissions(String userId, Set<String> codes) {
        UUID uuid = parseUuid(userId);

        var u = userRepo.findById(uuid).orElseThrow(() -> new NoSuchElementException("User not found"));

        // 1) Limpiar actuales (usa método alineado al EmbeddedId)
        userPermRepo.deleteByIdUserId(u.getUser_id());

        // 2) Insertar nuevos (resolviendo por code, validando existencia)
        for (String code : codes) {
            var p = permRepo.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("Unknown permission: " + code));

            var upId = new UserPermission.Id(u.getUser_id(), p.getPermissionId());
            var up = new UserPermission();
            up.setId(upId);
            up.setUser(u);
            up.setPermission(p);

            userPermRepo.save(up);
        }
    }

    // ---------- helpers ----------

    private static UUID parseUuid(String raw) {
        try {
            return UUID.fromString(raw);
        } catch (IllegalArgumentException ex) {
            throw new NoSuchElementException("Invalid userId: " + raw);
        }
    }
}