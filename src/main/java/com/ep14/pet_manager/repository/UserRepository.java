package com.ep14.pet_manager.repository;

import java.util.Optional;
import java.util.UUID;

import com.ep14.pet_manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, UUID> {

    Optional<User> findByEmail(String email);

}
