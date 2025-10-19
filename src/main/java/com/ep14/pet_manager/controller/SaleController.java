package com.ep14.pet_manager.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ep14.pet_manager.dto.SaleDTO;
import com.ep14.pet_manager.service.SaleService;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PreAuthorize("hasAuthority('sale.create')")
    @PostMapping
    public ResponseEntity<SaleDTO> registerSale(@RequestBody SaleDTO saleDTO) {
        return ResponseEntity.ok(saleService.registerSale(saleDTO));
    }

    @PreAuthorize("hasAuthority('sale.read')")
    @GetMapping
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @PreAuthorize("hasAuthority('sale.read')")
    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getSaleById(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('sale.read')")
    public ResponseEntity<List<SaleDTO>> getSalesByUser(@PathVariable UUID userId) {
        List<SaleDTO> sales = saleService.getSalesByUser(userId);
        return ResponseEntity.ok(sales);
    }
}
