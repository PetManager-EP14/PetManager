package com.ep14.pet_manager.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ep14.pet_manager.entity.Role;
import com.ep14.pet_manager.repository.UserPermissionRepository;
import com.ep14.pet_manager.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UsersDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserPermissionRepository userPermRepo;

    @PersistenceContext
    private EntityManager em;
    
    public UsersDetailsService(UserRepository userRepository, UserPermissionRepository userPermRepo){
        this.userRepository = userRepository;
        this.userPermRepo = userPermRepo;
    }

    @Override
    public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {
        com.ep14.pet_manager.entity.User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario No Encontrado!"));


        // 1) Permisos directos del usuario
        Set<String> codes = new HashSet<>(userPermRepo.findDirectCodesByUser(u.getUser_id()));

        // 2) Permisos por rol (consulta nativa hacia role_permissions + permissions)
        Role role = u.getRole();
        Long roleId = (role != null) ? role.getRole_id() : null;
        if (roleId != null) {
            @SuppressWarnings("unchecked")
            List<String> roleCodes = em.createNativeQuery(
                "select p.code " +
                "from role_permissions rp " +
                "join permissions p on p.permission_id = rp.permission_id " +
                "where rp.role_id = :rid")
                .setParameter("rid", roleId)
                .getResultList();
            codes.addAll(roleCodes);
        }

        // 3) Construir authorities a partir de los códigos de permiso
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String code : codes) {
            if (code != null && !code.isBlank()) {
                authorities.add(new SimpleGrantedAuthority(code.trim())); // ej: "purchase.read"
            }
        }

        // --- 4) Mantener authority de ROL para compatibilidad ---
        if (role != null && role.getCode() != null && !role.getCode().isBlank()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode().toUpperCase()));
        }

        return new User(u.getEmail(), u.getPassword_hash(), authorities);

    }
}
