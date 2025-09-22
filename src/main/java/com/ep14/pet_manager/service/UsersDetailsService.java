package com.ep14.pet_manager.service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ep14.pet_manager.entity.Permission;
import com.ep14.pet_manager.entity.Role;
import com.ep14.pet_manager.repository.UserPermissionRepository;
import com.ep14.pet_manager.repository.UserRepository;


@Service
public class UsersDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserPermissionRepository userPermRepo;

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

        // 2) Permisos por rol (desde la relación ManyToMany en Role)
        Role role = u.getRole();
        if (role != null && role.getPermissions() != null) {
            for (Permission p : role.getPermissions()) {
                if (p != null && p.getCode() != null && !p.getCode().isBlank()) {
                    codes.add(p.getCode().trim());
                }
            }
        }

        // 3) Authorities a partir de los códigos de permiso
        LinkedHashSet<GrantedAuthority> authorities = codes.stream()
            .filter(c -> c != null && !c.isBlank())
            .map(c -> new SimpleGrantedAuthority(c.trim()))
            .collect(Collectors.toCollection(LinkedHashSet::new));

        // 4) Mantener authority de ROL para compatibilidad (hasRole/hasAnyRole)
        if (role != null && role.getCode() != null && !role.getCode().isBlank()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode().toUpperCase()));
        }

        return new User(u.getEmail(), u.getPassword_hash(), authorities);

    }
}
