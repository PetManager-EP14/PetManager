package com.ep14.pet_manager.service;

import com.ep14.pet_manager.DTO.PurchaseDTO;
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

    public List<PurchaseDTO> getAllPurchases() {
        return purchaseRepository.findAll()
                .stream()
                .map(purchaseMapper::toDTO).toList();
    }

    public PurchaseDTO getPurchaseById(Long id) {
        return purchaseRepository.findById(id).map(purchaseMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
    }

    public PurchaseDTO createPurchase(PurchaseDTO purchaseDTO) {
        Purchase entity = purchaseMapper.toEntity(purchaseDTO);

        Supplier supplier = supplierRepository.findById(purchaseDTO.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + purchaseDTO.getSupplierId()));
        entity.setSupplier(supplier);

        User user = userRepository.findById(purchaseDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + purchaseDTO.getUserId()));
        entity.setUser(user);

        if (entity.getCreatedAt() == null) entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());

        Purchase saved = purchaseRepository.save(entity);
        return purchaseMapper.toDTO(saved);
    }
}
