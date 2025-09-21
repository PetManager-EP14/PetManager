package com.ep14.pet_manager.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ep14.pet_manager.entity.user;

@Repository
public interface UserRepository extends JpaRepository <user, UUID> {

    Optional<user> findByEmail(String email); 

}
