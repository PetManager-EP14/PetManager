package com.ep14.pet_manager.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime; 
import java.util.ArrayList; 
import java.util.List;
import java.util.UUID; 
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
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
    
    // Asumimos que la dependencia NotificationService está presente para la funcionalidad completa 
    private final NotificationService notificationService; 

    @Value("${notification.high-volume.threshold:10}") 
    private int highVolumeThreshold; 

    public SaleService(SaleRepository saleRepo,
                       ProductRepository productRepo,
                       UserRepository userRepo,
                       SaleMapper saleMapper,
                       NotificationService notificationService) { 
        this.saleRepo = saleRepo; 
        this.productRepo = productRepo; 
        this.userRepo = userRepo; 
        this.saleMapper = saleMapper; 
        this.notificationService = notificationService; 
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
        
        // asegurar que la lista de detalles no sea nula
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
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " +
                    detailDTO.getProductId())); 
            
            if (product.getStock().compareTo(detailDTO.getAmount()) < 0) { 
                throw new IllegalArgumentException("Stock insuficiente para el producto: " +
                    product.getName()); 
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
        
        // 6. Verificar si es una venta de alto volumen y enviar notificación
        checkAndNotifyHighVolumeSale(sale); 
        
        // Recargar la venta completa
        return saleMapper.toDTO(sale); 
    }

    /**
     * Verifica si una venta supera el umbral de alto volumen y envía notificación
     * @param sale La venta a verificar
     */
    private void checkAndNotifyHighVolumeSale(Sale sale) { 
        // Calcular la cantidad total de productos vendidos
        BigDecimal totalQuantity = sale.getSaleDetails().stream() 
            .map(SaleDetails::getAmount) 
            .reduce(BigDecimal.ZERO, BigDecimal::add); 
        
        if (logger.isLoggable(java.util.logging.Level.INFO)) { 
            logger.info(String.format("Venta ID %d - Cantidad total: %s - Umbral: %d", 
                sale.getSaleId(), totalQuantity, highVolumeThreshold)); 
        }
        
        // Si supera el umbral, crear notificación
        if (totalQuantity.compareTo(BigDecimal.valueOf(highVolumeThreshold)) > 0) { 
            logger.info(String.format("¡Venta de alto volumen detectada! ID: %d, Cantidad: %s", 
                sale.getSaleId(), totalQuantity)); 
            try {
                notificationService.createHighVolumeNotification(sale); 
            } catch (Exception e) {
                // Log el error pero no fallar la transacción de venta
                logger.warning("Error al crear notificación de alto volumen: " + e.getMessage()); 
            }
        }
    }

    // ** MÉTODO CORREGIDO PARA USAR FETCH JOIN **
    public List<SaleDTO> getAllSales() {
        // usamos el método que cargará los detalles: findAllWithDetails()
        List<Sale> sales = saleRepo.findAllWithDetails(); // Asumiendo que este método existe en SaleRepository.java
        return sales.stream().map(saleMapper::toDTO).toList();
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
            sales = saleRepo.findByUserAndDateRange(userId, OffsetDateTime.parse(startDate), 
                OffsetDateTime.parse(endDate));
        } else if (userId != null) { 
            sales = saleRepo.findByUser_UserId(userId); 
        } else if (startDate != null && endDate != null) { 
            sales = saleRepo.findByDateBetween(OffsetDateTime.parse(startDate), 
                OffsetDateTime.parse(endDate)); 
        } else {
            // Se utiliza findAll() para el filtro sin parámetros, aunque para la consulta general se usa findAllWithDetails()
            sales = saleRepo.findAllWithDetails();
        }
        return sales.stream().map(saleMapper::toDTO).toList();
    }
}