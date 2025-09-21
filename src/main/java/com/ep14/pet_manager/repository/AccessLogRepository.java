package com.ep14.pet_manager.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ep14.pet_manager.entity.AccessLog;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

    @Query("""
        select a from AccessLog a
        where (:userId is null or a.userId = :userId)
          and (:fromTs is null or a.createdAt >= :fromTs)
          and (:toTs   is null or a.createdAt <  :toTs)
        order by a.createdAt desc
    """)
    List<AccessLog> search(@Param("userId") UUID userId,
                           @Param("fromTs") Instant from,
                           @Param("toTs") Instant to);
}