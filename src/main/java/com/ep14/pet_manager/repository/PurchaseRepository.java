package com.ep14.pet_manager.repository;

import com.ep14.pet_manager.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    
    @Query("SELECT p FROM Purchase p JOIN FETCH p.purchaseDetails pd JOIN FETCH pd.product")
    List<Purchase> findAllWithDetailsAndProduct();
}