package com.ep14.pet_manager.service;

import com.ep14.pet_manager.DTO.PurchaseDTO;
import com.ep14.pet_manager.entity.Purchase;
import com.ep14.pet_manager.mapper.PurchaseMapper;
import com.ep14.pet_manager.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseMapper purchaseMapper) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
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
        Purchase purchase = purchaseMapper.toEntity(purchaseDTO);
        return purchaseMapper.toDTO(purchaseRepository.save(purchase));
    }
}
