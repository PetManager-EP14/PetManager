package com.ep14.pet_manager.repository;

import com.ep14.pet_manager.entity.Supplier;
import com.ep14.pet_manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
