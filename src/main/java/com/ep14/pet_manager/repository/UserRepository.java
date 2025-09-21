package com.ep14.pet_manager.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ep14.pet_manager.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    // Buscar usuario por email (para login o validación)
    Optional<User> findByEmail(String email);

    // Verificar si ya existe un email registrado
    boolean existsByEmail(String email);

    @Query("select u from User u where u.role_id = :roleId")
    Optional<User> findByRoleId(@Param("roleId") Long roleId);
}
