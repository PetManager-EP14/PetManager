package com.ep14.pet_manager.controller;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ep14.pet_manager.assembler.NotificationModelAssembler;
import com.ep14.pet_manager.entity.SaleNotification;
import com.ep14.pet_manager.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "API para gestión de notificaciones de ventas")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationModelAssembler assembler;

    public NotificationController(NotificationService notificationService, NotificationModelAssembler assembler) {
        this.notificationService = notificationService;
        this.assembler = assembler;
    }

    @GetMapping("/sale/{saleId}")
    @PreAuthorize("hasAuthority('sale.read')")
    @Operation(summary = "Obtener notificaciones de una venta", 
               description = "Retorna todas las notificaciones asociadas a una venta específica")
    public ResponseEntity<CollectionModel<EntityModel<SaleNotification>>> getNotificationsBySale(@PathVariable Long saleId) {
        List<SaleNotification> notifications = notificationService.getNotificationsBySale(saleId);
        return ResponseEntity.ok(assembler.toCollectionModel(notifications));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('sale.read')")
    @Operation(summary = "Obtener notificaciones por tipo", 
               description = "Retorna todas las notificaciones filtradas por tipo")
    public ResponseEntity<CollectionModel<EntityModel<SaleNotification>>> getNotificationsByType(
            @RequestParam(required = false) SaleNotification.type type) {
        
        if (type != null) {
            List<SaleNotification> notifications = notificationService.getNotificationsByType(type);
            return ResponseEntity.ok(assembler.toCollectionModel(notifications));
        }
        
        // Si no se especifica tipo, retornar las de alto volumen por defecto
        List<SaleNotification> notifications = notificationService.getNotificationsByType(SaleNotification.type.HIGH_VOLUMEN);
        return ResponseEntity.ok(assembler.toCollectionModel(notifications));
    }
}
