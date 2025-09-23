package com.ep14.pet_manager.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import com.ep14.pet_manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
  
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, UUID> {
    
    // Buscar usuario por emial (para login o validación)
    Optional<User> findByEmail(String email);
    
    // Verificar si ya existe un email registrado
    boolean existsByEmail(String email);
    @Query("select u from User u where u.role.roleId = :roleId")
    List<User> findAllByRoleId(@Param("roleId") Long roleId);

}
