package com.ep14.pet_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ep14.pet_manager.entity.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {}
