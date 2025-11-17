package com.ep14.pet_manager.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ep14.pet_manager.entity.Sale;
import com.ep14.pet_manager.entity.SaleNotification;
import com.ep14.pet_manager.repository.SaleNotificationRepository;

/**
 * Servicio para gestionar notificaciones de ventas
 */
@Service
public class NotificationService {

    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());

    private final SaleNotificationRepository saleNotificationRepository;
    private final EmailService emailService;

    public NotificationService(SaleNotificationRepository saleNotificationRepository,
                             EmailService emailService) {
        this.saleNotificationRepository = saleNotificationRepository;
        this.emailService = emailService;
    }

    /**
     * Crea y envía una notificación de venta de alto volumen
     * @param sale La venta que generó la notificación
     * @return La notificación creada
     */
    @Transactional
    public SaleNotification createHighVolumeNotification(Sale sale) {
        logger.info("Creando notificación de alto volumen para venta ID: " + sale.getSaleId());
        
        SaleNotification notification = new SaleNotification();
        notification.setSale(sale);
        notification.setMedia(SaleNotification.media.EMAIL);
        notification.setType(SaleNotification.type.HIGH_VOLUMEN);
        notification.setShippingDate(OffsetDateTime.now());
        notification.setCreatedAt(OffsetDateTime.now());
        
        // Guardar la notificación
        notification = saleNotificationRepository.save(notification);
        
        // Enviar la notificación por correo electrónico
        try {
            emailService.sendHighVolumeSaleNotification(sale);
            logger.info("Notificación de alto volumen enviada exitosamente para venta ID: " + sale.getSaleId());
        } catch (Exception e) {
            logger.warning("Error al enviar notificación por email: " + e.getMessage());
            // Continúa aunque falle el envío del email - la notificación ya está registrada
        }
        
        return notification;
    }

    /**
     * Obtiene todas las notificaciones de una venta
     */
    public List<SaleNotification> getNotificationsBySale(Long saleId) {
        return saleNotificationRepository.findBySale_SaleId(saleId);
    }

    /**
     * Obtiene todas las notificaciones por tipo
     */
    public List<SaleNotification> getNotificationsByType(SaleNotification.type type) {
        return saleNotificationRepository.findByType(type);
    }
}
