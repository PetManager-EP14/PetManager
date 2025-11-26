package com.ep14.pet_manager.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ep14.pet_manager.assembler.SaleModelAssembler;
import com.ep14.pet_manager.dto.SaleDTO;
import com.ep14.pet_manager.service.SaleService;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;
    private final SaleModelAssembler assembler;

    public SaleController(SaleService saleService, SaleModelAssembler assembler) {
        this.saleService = saleService;
        this.assembler = assembler;
    }

    @PreAuthorize("hasAuthority('sale.create')")
    @PostMapping
    public ResponseEntity<EntityModel<SaleDTO>> registerSale(@RequestBody SaleDTO saleDTO) {
        SaleDTO created = saleService.registerSale(saleDTO);
        return ResponseEntity.ok(assembler.toModel(created));
    }

    @PreAuthorize("hasAuthority('sale.read')")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<SaleDTO>>> getAllSales() {
        List<SaleDTO> sales = saleService.getAllSales();
        return ResponseEntity.ok(assembler.toCollectionModel(sales));
    }

    @PreAuthorize("hasAuthority('sale.read')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SaleDTO>> getSaleById(@PathVariable Long id) {
        SaleDTO sale = saleService.getSaleById(id);
        return ResponseEntity.ok(assembler.toModel(sale));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('sale.read')")
    public ResponseEntity<CollectionModel<EntityModel<SaleDTO>>> getSalesByUser(@PathVariable UUID userId) {
        List<SaleDTO> sales = saleService.getSalesByUser(userId);
        return ResponseEntity.ok(assembler.toCollectionModel(sales));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('sale.read')")
    public ResponseEntity<CollectionModel<EntityModel<SaleDTO>>> getAllSalesFiltered(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long saleId) {

        List<SaleDTO> sales = saleService.getAllSalesFiltered(userId, startDate, endDate, saleId);
        return ResponseEntity.ok(assembler.toCollectionModel(sales));
    }
}