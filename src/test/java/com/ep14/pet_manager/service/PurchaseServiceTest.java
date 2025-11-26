package com.ep14.pet_manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ep14.pet_manager.dto.PurchaseDTO;
import com.ep14.pet_manager.entity.Purchase;
import com.ep14.pet_manager.entity.Supplier;
import com.ep14.pet_manager.entity.User;
import com.ep14.pet_manager.mapper.PurchaseMapper;
import com.ep14.pet_manager.repository.PurchaseRepository;
import com.ep14.pet_manager.repository.SupplierRepository;
import com.ep14.pet_manager.repository.UserRepository;

class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private PurchaseMapper purchaseMapper;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private UserRepository userRepository;

    private PurchaseService purchaseService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        purchaseService = new PurchaseService(purchaseRepository, purchaseMapper, supplierRepository, userRepository);
    }

    // 1. Devuelve todas las compras correctamente mapeadas
    @Test
    void getAllPurchases_shouldReturnMappedList() {
        Purchase purchase = new Purchase();
        PurchaseDTO dto = new PurchaseDTO();

        when(purchaseRepository.findAllWithDetailsAndProduct()).thenReturn(List.of(purchase));
        when(purchaseMapper.toDTO(purchase)).thenReturn(dto);

        List<PurchaseDTO> result = purchaseService.getAllPurchases();

        assertThat(result).hasSize(1).contains(dto);
        verify(purchaseRepository).findAllWithDetailsAndProduct();
        verify(purchaseMapper).toDTO(purchase);
    }

    // 2. Devuelve una compra por ID si existe
    @Test
    void getPurchaseById_shouldReturnPurchaseDTO_whenExists() {
        Purchase purchase = new Purchase();
        PurchaseDTO dto = new PurchaseDTO();

        when(purchaseRepository.findById(1L)).thenReturn(Optional.of(purchase));
        when(purchaseMapper.toDTO(purchase)).thenReturn(dto);

        PurchaseDTO result = purchaseService.getPurchaseById(1L);

        assertThat(result).isEqualTo(dto);
        verify(purchaseRepository).findById(1L);
        verify(purchaseMapper).toDTO(purchase);
    }

    // 3. Lanza excepción si la compra no existe
    @Test
    void getPurchaseById_shouldThrowException_whenNotFound() {
        when(purchaseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> purchaseService.getPurchaseById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Compra no encontrada");
    }

    // 4. Crea una compra correctamente con proveedor y usuario válidos
    @Test
    void createPurchase_shouldSaveAndReturnDTO_whenValid() {
        UUID userId = java.util.UUID.randomUUID();

        PurchaseDTO dto = new PurchaseDTO();
        dto.setSupplierId(10L);
        dto.setUserId(userId);

        Supplier supplier = new Supplier();
        supplier.setSupplierId(10L);

        User user = new User();
        user.setUserId(userId);

        Purchase entity = new Purchase();
        Purchase saved = new Purchase();

        when(purchaseMapper.toEntity(dto)).thenReturn(entity);
        when(supplierRepository.findById(10L)).thenReturn(Optional.of(supplier));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(saved);
        when(purchaseMapper.toDTO(saved)).thenReturn(dto);

        PurchaseDTO result = purchaseService.createPurchase(dto);

        ArgumentCaptor<Purchase> captor = ArgumentCaptor.forClass(Purchase.class);
        verify(purchaseRepository).save(captor.capture());
        Purchase captured = captor.getValue();

        assertThat(captured.getSupplier()).isEqualTo(supplier);
        assertThat(captured.getUser()).isEqualTo(user);
        assertThat(result).isEqualTo(dto);
    }

    // 5. Lanza excepción si el proveedor no existe
    @Test
    void createPurchase_shouldThrowException_whenSupplierNotFound() {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setSupplierId(99L);
        dto.setUserId(java.util.UUID.randomUUID());

        when(purchaseMapper.toEntity(dto)).thenReturn(new Purchase());
        when(supplierRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> purchaseService.createPurchase(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Proveedor no encontrado");
    }

    // 6. Lanza excepción si el usuario no existe

    @Test
    void createPurchase_shouldThrowException_whenUserNotFound() {
        UUID missingUserId = java.util.UUID.randomUUID();

        PurchaseDTO dto = new PurchaseDTO();
        dto.setSupplierId(1L);
        dto.setUserId(missingUserId);

        when(purchaseMapper.toEntity(dto)).thenReturn(new Purchase());
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(new Supplier()));
        when(userRepository.findById(missingUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> purchaseService.createPurchase(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void updatePurchase_shouldUpdateAndReturnDTO_whenValid() {
        UUID userId = UUID.randomUUID();
        Purchase existingPurchase = new Purchase();
        existingPurchase.setPurchaseId(1L);

        Supplier existingSupplier = new Supplier();
        existingSupplier.setSupplierId(20L);
        existingPurchase.setSupplier(existingSupplier);

        User existingUser = new User();
        existingUser.setUserId(userId);
        existingPurchase.setUser(existingUser);
        existingPurchase.setCreatedAt(OffsetDateTime.now().minusDays(1));

        PurchaseDTO dto = new PurchaseDTO();
        dto.setDate(OffsetDateTime.now());
        dto.setStatus(PurchaseDTO.StatusShopping.REGISTERED);
        dto.setTotal(BigDecimal.valueOf(2000.00));
        dto.setSupplierId(20L);
        dto.setUserId(userId);

        Purchase updatedEntity = new Purchase();
        updatedEntity.setSupplier(existingSupplier);

        Supplier supplier = new Supplier();
        supplier.setSupplierId(20L);

        PurchaseDTO responseDTO = new PurchaseDTO();
        responseDTO.setId(1L);
        responseDTO.setStatus(PurchaseDTO.StatusShopping.REGISTERED);

        when(purchaseRepository.findById(1L)).thenReturn(Optional.of(existingPurchase));
        when(purchaseMapper.toEntity(dto)).thenReturn(updatedEntity);
        when(supplierRepository.findById(20L)).thenReturn(Optional.of(supplier));
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(existingPurchase);
        when(purchaseMapper.toDTO(existingPurchase)).thenReturn(responseDTO);

        PurchaseDTO result = purchaseService.updatePurchase(1L, dto);

        verify(purchaseRepository).save(any(Purchase.class));
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void updatePurchase_shouldThrowException_whenNotFound() {
        PurchaseDTO dto = new PurchaseDTO();

        when(purchaseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> purchaseService.updatePurchase(1L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Compra no encontrada");
    }

    @Test
    void deletePurchase_shouldDelete_whenExists() {
        when(purchaseRepository.existsById(1L)).thenReturn(true);

        purchaseService.deletePurchase(1L);

        verify(purchaseRepository).deleteById(1L);
    }

    @Test
    void deletePurchase_shouldThrowException_whenNotFound() {
        when(purchaseRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> purchaseService.deletePurchase(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Compra no encontrada");
    }
}
