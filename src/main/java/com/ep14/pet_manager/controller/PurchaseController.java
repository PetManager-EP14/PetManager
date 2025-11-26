package com.ep14.pet_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ep14.pet_manager.assembler.PurchaseModelAssembler;
import com.ep14.pet_manager.dto.PurchaseDTO;
import com.ep14.pet_manager.service.PurchaseService;

@RestController
@RequestMapping(value = "/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final PurchaseModelAssembler assembler;

    @Autowired
    public PurchaseController(PurchaseService purchaseService, PurchaseModelAssembler assembler) {
        this.purchaseService = purchaseService;
        this.assembler = assembler;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'purchase.read')")
    @GetMapping

    public ResponseEntity<CollectionModel<EntityModel<PurchaseDTO>>> getAllPurchases() {
        List<PurchaseDTO> purchases = purchaseService.getAllPurchases();
        return ResponseEntity.ok(assembler.toCollectionModel(purchases));
    }

    // Para las pruebas de integracion (hay que modificar despues)

    /*
     * @PreAuthorize("hasAuthority('purchase.read')")
     * 
     * @GetMapping
     * public ResponseEntity<List<PurchaseDTO>> getAllPurchases(){
     * return ResponseEntity.ok(purchaseService.getAllPurchases());
     * }
     */

    @PreAuthorize("hasAuthority('purchase.read')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PurchaseDTO>> getPurchaseById(@PathVariable Long id) {
        PurchaseDTO purchase = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(assembler.toModel(purchase));
    }

    @PreAuthorize("hasAuthority('purchase.create')")
    @PostMapping
    public ResponseEntity<?> createPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        if (purchaseDTO.getStatus() == null) {
            return ResponseEntity.badRequest().body("El estado es obligatorio");
        }
        PurchaseDTO created = purchaseService.createPurchase(purchaseDTO);
        return ResponseEntity.ok(assembler.toModel(created));
    }

    @PreAuthorize("hasAuthority('purchase.update')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PurchaseDTO>> updatePurchase(@PathVariable Long id,
            @RequestBody PurchaseDTO purchaseDTO) {
        PurchaseDTO update = purchaseService.updatePurchase(id, purchaseDTO);
        return ResponseEntity.ok(assembler.toModel(update));
    }

    @PreAuthorize("hasAuthority('purchase.delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.ok().build();
    }
}