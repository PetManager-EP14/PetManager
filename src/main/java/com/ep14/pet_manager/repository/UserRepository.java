package com.ep14.pet_manager.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ep14.pet_manager.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    // Buscar usuario por email (para login o validación)
    Optional<User> findByEmail(String email);

    // Verificar si ya existe un email registrado
    boolean existsByEmail(String email);

    // Buscar usuarios por rol
    Optional<User> findByRole_id(Long role_id);
}
