package com.ep14.pet_manager.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ep14.pet_manager.repository.PurchaseRepository;
import com.ep14.pet_manager.mapper.PurchaseMapper;
import com.ep14.pet_manager.dto.PurchaseDTO;
import com.ep14.pet_manager.entity.Purchase;

class PurchaseServiceTests {

    @Mock
    PurchaseRepository purchaseRepository;

    @Mock
    PurchaseMapper purchaseMapper;

    @InjectMocks
    PurchaseService purchaseService;

    @Test
    void getAllPurchases_returnsMappedDTOs() {
        Purchase purchase = new Purchase();
        PurchaseDTO dto = new PurchaseDTO();
        when(purchaseRepository.findAll()).thenReturn(List.of(purchase));
        when(purchaseMapper.toDTO(purchase)).thenReturn(dto);

        List<PurchaseDTO> result = purchaseService.getAllPurchases();
        assertThat(result).hasSize(1);
    }

    @Test
    void getPurchaseById_notFound_throws() {
        when(purchaseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> purchaseService.getPurchaseById(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Compra no encontrada");
    }
}