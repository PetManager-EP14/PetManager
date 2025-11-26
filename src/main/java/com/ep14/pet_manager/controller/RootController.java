package com.ep14.pet_manager.controller;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Root", description = "Punto de entrada principal de la API con enlaces HATEOAS")
public class RootController {

    @GetMapping
    @Operation(summary = "Obtener enlaces principales de la API", 
               description = "Retorna los enlaces HATEOAS a todos los recursos disponibles en la API")
    public ResponseEntity<RepresentationModel<?>> root() {
        RepresentationModel<?> model = new RepresentationModel<>();
        
        // Agregar enlaces manualmente para evitar problemas de autorización
        model.add(Link.of("/api/notifications", "notifications"));
        model.add(Link.of("/api/sales", "sales"));
        model.add(Link.of("/api/purchases", "purchases"));
        model.add(Link.of("/api", "self"));
        
        return ResponseEntity.ok(model);
    }
}
