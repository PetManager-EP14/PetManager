package com.ep14.pet_manager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ep14.pet_manager.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByCode(String code);

    @Query("select p.code from Permission p")
    List<String> findAllCodes();
}