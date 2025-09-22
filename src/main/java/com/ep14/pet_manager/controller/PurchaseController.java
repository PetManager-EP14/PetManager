package com.ep14.pet_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ep14.pet_manager.DTO.PurchaseDTO;
import com.ep14.pet_manager.service.PurchaseService;

@RestController
@RequestMapping(value = "/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PreAuthorize("hasAuthority('purchase.read')")
    @GetMapping
    public ResponseEntity<List<PurchaseDTO>> getAllPurchases(){
        return ResponseEntity.ok(purchaseService.getAllPurchases());
    }

    @PreAuthorize("hasAuthority('purchase.read')")
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDTO> getPurchaseById(@PathVariable Long id){
        return ResponseEntity.ok(purchaseService.getPurchaseById(id));
    }

    @PreAuthorize("hasAuthority('purchase.create')")
    @PostMapping
    public ResponseEntity<?> createPurchase(@RequestBody PurchaseDTO purchaseDTO){
        if (purchaseDTO.getStatus() == null) {
            return ResponseEntity.badRequest().body("El estado es obligatorio");
        }
        return ResponseEntity.ok(purchaseService.createPurchase(purchaseDTO));
    }
}
