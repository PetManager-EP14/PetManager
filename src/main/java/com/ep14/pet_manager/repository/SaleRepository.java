package com.ep14.pet_manager.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ep14.pet_manager.entity.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByUser_UserId(UUID userId);

    List<Sale> findByDateBetween(OffsetDateTime start, OffsetDateTime end);

    @Query("SELECT s FROM Sale s WHERE s.user.userId = :userId AND s.date BETWEEN :start AND :end")
    List<Sale> findByUserAndDateRange(@Param("userId") UUID userId,
                                    @Param("start") OffsetDateTime start,
                                    @Param("end") OffsetDateTime end);

}
