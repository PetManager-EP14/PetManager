package com.ep14.pet_manager.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ep14.pet_manager.dto.SaleDTO;
import com.ep14.pet_manager.dto.SaleDetailDTO;
import com.ep14.pet_manager.entity.Product;
import com.ep14.pet_manager.entity.Sale;
import com.ep14.pet_manager.entity.SaleDetails;
import com.ep14.pet_manager.entity.User;
import com.ep14.pet_manager.mapper.SaleMapper;
import com.ep14.pet_manager.repository.ProductRepository;
import com.ep14.pet_manager.repository.SaleDetailsRepository;
import com.ep14.pet_manager.repository.SaleRepository;
import com.ep14.pet_manager.repository.UserRepository;

@Service
public class SaleService {
    private final SaleRepository saleRepo;
    private final SaleDetailsRepository saleDetailsRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final SaleMapper saleMapper;

    public SaleService(SaleRepository saleRepo, SaleDetailsRepository saleDetailsRepo,
                       ProductRepository productRepo, UserRepository userRepo, SaleMapper saleMapper) {
        this.saleRepo = saleRepo;
        this.saleDetailsRepo = saleDetailsRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.saleMapper = saleMapper;
    }

    @Transactional
    public SaleDTO registerSale(SaleDTO dto) {
        // 1. Validar usuario
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // 2. Crear entidad Sale
        Sale sale = saleMapper.toEntity(dto);
        sale.setUser(user);
        sale.setStatus(Sale.saleStatus.REGISTERED);
        sale.setDate(OffsetDateTime.now());
        sale.setCreatedAt(OffsetDateTime.now());
        sale.setUpdatedAt(OffsetDateTime.now());
        sale.setTotal(sale.getTotal() == null ? BigDecimal.ZERO : sale.getTotal());
        
        sale.setMethod(dto.getMethod() != null 
            ? Sale.paymentMethod.valueOf(dto.getMethod().toUpperCase()) 
            : Sale.paymentMethod.CASH);

        // 3️. Guardar primero la venta para generar ID
        sale = saleRepo.save(sale);

        BigDecimal total = BigDecimal.ZERO;

        // 4. Procesar los detalles
        for (SaleDetailDTO detailDTO : dto.getDetails()) {
            Product product = productRepo.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + detailDTO.getProductId()));

            if (product.getStock().compareTo(detailDTO.getAmount()) < 0) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + product.getName());
            }

            product.setStock(product.getStock().subtract(detailDTO.getAmount()));
            productRepo.save(product);

            BigDecimal subTotal = product.getPriceSale().multiply(detailDTO.getAmount());
            total = total.add(subTotal);

            SaleDetails detail = new SaleDetails();
            detail.setSale(sale);
            detail.setProduct(product);
            detail.setAmount(detailDTO.getAmount());
            saleDetailsRepo.save(detail);
        }

        // 5. Actualizar venta con total final
        sale.setTotal(total);
        sale.setUpdatedAt(OffsetDateTime.now());
        sale = saleRepo.save(sale);

        // 6. Retornar DTO
        return saleMapper.toDTO(sale);
    }

    public List<SaleDTO> getAllSales() {
        return saleRepo.findAll().stream().map(saleMapper::toDTO).toList();
    }

    public SaleDTO getSaleById(Long id) {
        return saleRepo.findById(id).map(saleMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }
}