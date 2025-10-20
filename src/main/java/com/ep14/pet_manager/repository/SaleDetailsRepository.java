package com.ep14.pet_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ep14.pet_manager.entity.SaleDetails;

@Repository
public interface SaleDetailsRepository extends JpaRepository<SaleDetails, Long> {}
