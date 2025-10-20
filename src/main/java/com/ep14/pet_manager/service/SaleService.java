package com.ep14.pet_manager.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

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
import com.ep14.pet_manager.repository.SaleRepository;
import com.ep14.pet_manager.repository.UserRepository;

@Service
public class SaleService {

    private static final Logger logger = Logger.getLogger(SaleService.class.getName());

    private final SaleRepository saleRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final SaleMapper saleMapper;

    public SaleService(SaleRepository saleRepo,
                        ProductRepository productRepo, 
                        UserRepository userRepo, 
                        SaleMapper saleMapper) {
        this.saleRepo = saleRepo;
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


        // asegurar que la lista no sea nula
        if (sale.getSaleDetails() == null) {
            sale.setSaleDetails(new ArrayList<>());
        }

        // Guarda la venta y fuerza la escritura inmediata en la base
        sale = saleRepo.saveAndFlush(sale);

        if (logger.isLoggable(java.util.logging.Level.INFO)){
            logger.info(String.valueOf(sale.getSaleId()));
        }

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
            detail.setProduct(product);
            detail.setAmount(detailDTO.getAmount());
            detail.setCreatedAt(OffsetDateTime.now());
            detail.setSale(sale);
            
            sale.getSaleDetails().add(detail);
        }

        // 5. Actualizar venta con total final
        sale.setTotal(total);
        sale.setUpdatedAt(OffsetDateTime.now());

        // guardamos
        sale = saleRepo.saveAndFlush(sale);

        // Recargar la venta completa
        return saleMapper.toDTO(sale);
    }

    public List<SaleDTO> getAllSales() {
        return saleRepo.findAll().stream().map(saleMapper::toDTO).toList();
    }

    public SaleDTO getSaleById(Long id) {
        return saleRepo.findById(id).map(saleMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    public List<SaleDTO> getSalesByUser(UUID userId) {
        List<Sale> sales = saleRepo.findByUser_UserId(userId);
        if (sales.isEmpty()) {
            throw new IllegalArgumentException("El usuario no tiene ventas registradas.");
        }
        return sales.stream()
                .map(saleMapper::toDTO)
                .toList();
    }

    public List<SaleDTO> getAllSalesFiltered(UUID userId, String startDate, String endDate, Long saleId) {
        List<Sale> sales;

        if (saleId != null) {
            sales = saleRepo.findById(saleId).map(List::of).orElse(List.of());
        } else if (userId != null && startDate != null && endDate != null) {
            sales = saleRepo.findByUserAndDateRange(userId, OffsetDateTime.parse(startDate), OffsetDateTime.parse(endDate));
        } else if (userId != null) {
            sales = saleRepo.findByUser_UserId(userId);
        } else if (startDate != null && endDate != null) {
            sales = saleRepo.findByDateBetween(OffsetDateTime.parse(startDate), OffsetDateTime.parse(endDate));
        } else {
            sales = saleRepo.findAll();
        }

        return sales.stream().map(saleMapper::toDTO).toList();
    }
}