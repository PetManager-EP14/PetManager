package com.ep14.pet_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ep14.pet_manager.entity.SaleNotification;

@Repository
public interface SaleNotificationRepository extends JpaRepository<SaleNotification, Long> {
    
    /**
     * Busca todas las notificaciones asociadas a una venta específica
     */
    List<SaleNotification> findBySale_SaleId(Long saleId);
    
    /**
     * Busca notificaciones por tipo
     */
    List<SaleNotification> findByType(SaleNotification.type type);
    
    /**
     * Busca notificaciones por medio de envío
     */
    List<SaleNotification> findByMedia(SaleNotification.media media);
}
