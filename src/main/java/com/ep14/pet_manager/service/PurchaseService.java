package com.ep14.pet_manager.service;

import com.ep14.pet_manager.dto.PurchaseDTO;
import com.ep14.pet_manager.entity.Purchase;
import com.ep14.pet_manager.entity.Supplier;
import com.ep14.pet_manager.entity.User;
import com.ep14.pet_manager.mapper.PurchaseMapper;
import com.ep14.pet_manager.repository.PurchaseRepository;
import com.ep14.pet_manager.repository.SupplierRepository;
import com.ep14.pet_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository,
                           PurchaseMapper purchaseMapper,
                           SupplierRepository supplierRepository,
                           UserRepository userRepository) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }

    /**
     * Obtiene todas las compras.
     */
    public List<PurchaseDTO> getAllPurchases() {
        // Se reemplaza purchaseRepository.findAll() [12] por el método optimizado
        List<Purchase> purchases = purchaseRepository.findAllWithDetailsAndProduct(); 

        return purchases.stream()
                .map(purchaseMapper::toDTO)
                .toList();
    }

    /**
     * Obtiene una compra por su ID.
     */
    public PurchaseDTO getPurchaseById(Long id) {
        return purchaseRepository.findById(id).map(purchaseMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada")); 
    }

    /**
     * Registra una nueva compra.
     */
    public PurchaseDTO createPurchase(PurchaseDTO purchaseDTO) {
        Purchase entity = purchaseMapper.toEntity(purchaseDTO); 

        // Validación y asignación de Proveedor [13]
        Supplier supplier = supplierRepository.findById(purchaseDTO.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + purchaseDTO.getSupplierId()));
        entity.setSupplier(supplier);

        // Validación y asignación de Usuario [13, 14]
        User user = userRepository.findById(purchaseDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + purchaseDTO.getUserId()));
        entity.setUser(user);

        // Asignación de fechas [14]
        if (entity.getCreatedAt() == null)
            entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());

        Purchase saved = purchaseRepository.save(entity); 
        return purchaseMapper.toDTO(saved);
    }

    /**
     * Actualiza una compra existente por ID.
     */
    public PurchaseDTO updatePurchase(Long id, PurchaseDTO purchaseDTO) {
        Purchase existingPurchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada para actualizar: " + id));

        // Mapea los campos del DTO a la entidad existente
        Purchase updatedEntity = purchaseMapper.toEntity(purchaseDTO);
        updatedEntity.setPurchaseId(id); // Asegura que la ID sea la correcta

        // Validación y reasignación de Proveedor (si es necesario)
        if (!updatedEntity.getSupplier().getSupplierId().equals(existingPurchase.getSupplier().getSupplierId())) {
            Supplier supplier = supplierRepository.findById(updatedEntity.getSupplier().getSupplierId())
                    .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + updatedEntity.getSupplier().getSupplierId()));
            updatedEntity.setSupplier(supplier);
        } else {
            updatedEntity.setSupplier(existingPurchase.getSupplier());
        }
        
        // Mantener el usuario original si el DTO no lo incluye o si el mapper solo usa la ID
        updatedEntity.setUser(existingPurchase.getUser());
        
        // Actualizar fechas
        updatedEntity.setCreatedAt(existingPurchase.getCreatedAt());
        updatedEntity.setUpdatedAt(OffsetDateTime.now());

        Purchase saved = purchaseRepository.save(updatedEntity);
        return purchaseMapper.toDTO(saved);
    }

    public void deletePurchase(Long id) {
        if (!purchaseRepository.existsById(id)) {
            throw new RuntimeException("Compra no encontrada para eliminar: " + id);
        }
        purchaseRepository.deleteById(id);
    }
}
