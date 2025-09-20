package com.ep14.pet_manager.repository;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ep14.pet_manager.entity.UserPermission;

public interface UserPermissionRepository extends JpaRepository<UserPermission, UserPermission.Id> {

    @Query("""
       select up.permission.code
       from UserPermission up
       where up.user.user_id = :userId
    """)
    Set<String> findDirectCodesByUser(UUID userId);

    void deleteByUser_User_id(UUID userId);
}