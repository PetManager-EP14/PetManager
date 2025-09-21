package com.ep14.pet_manager.controller;

import com.ep14.pet_manager.DTO.PurchaseDTO;
import com.ep14.pet_manager.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<List<PurchaseDTO>> getAllPurchases(){
        return ResponseEntity.ok(purchaseService.getAllPurchases());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDTO> getPurchaseById(@PathVariable Long id){
        return ResponseEntity.ok(purchaseService.getPurchaseById(id));
    }

    @PostMapping
    public ResponseEntity<?> createPurchase(@RequestBody PurchaseDTO purchaseDTO){
        if (purchaseDTO.getStatus() == null) {
            return ResponseEntity.badRequest().body("El estado es obligatorio");
        }
        return ResponseEntity.ok(purchaseService.createPurchase(purchaseDTO));
    }
}
