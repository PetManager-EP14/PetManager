package com.ep14.pet_manager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ep14.pet_manager.entity.SaleNotification;
import com.ep14.pet_manager.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "API para gestión de notificaciones de ventas")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/sale/{saleId}")
    @PreAuthorize("hasAuthority('sale.read')")
    @Operation(summary = "Obtener notificaciones de una venta", 
               description = "Retorna todas las notificaciones asociadas a una venta específica")
    public ResponseEntity<List<SaleNotification>> getNotificationsBySale(@PathVariable Long saleId) {
        List<SaleNotification> notifications = notificationService.getNotificationsBySale(saleId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('sale.read')")
    @Operation(summary = "Obtener notificaciones por tipo", 
               description = "Retorna todas las notificaciones filtradas por tipo")
    public ResponseEntity<List<SaleNotification>> getNotificationsByType(
            @RequestParam(required = false) SaleNotification.type type) {
        
        if (type != null) {
            return ResponseEntity.ok(notificationService.getNotificationsByType(type));
        }
        
        // Si no se especifica tipo, retornar las de alto volumen por defecto
        return ResponseEntity.ok(
            notificationService.getNotificationsByType(SaleNotification.type.HIGH_VOLUMEN)
        );
    }
}
