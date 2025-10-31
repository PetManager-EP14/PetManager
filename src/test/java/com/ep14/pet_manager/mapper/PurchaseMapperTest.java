package com.ep14.pet_manager.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ep14.pet_manager.dto.PurchaseDTO;
import com.ep14.pet_manager.entity.Purchase;
import com.ep14.pet_manager.entity.Supplier;
import com.ep14.pet_manager.entity.User;

class PurchaseMapperTest {

    private PurchaseMapper mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(PurchaseMapper.class);
    }

    @Test
    void toDTO_shouldMapEntityToDTO() {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(10L);

        User user = new User();
        user.setUserId(UUID.randomUUID());

        Purchase purchase = new Purchase();
        purchase.setPurchaseId(1L);
        purchase.setSupplier(supplier);
        purchase.setUser(user);
        purchase.setStatus(Purchase.statusShopping.DRAFT);
        purchase.setTotal(BigDecimal.valueOf(1000.50));

        PurchaseDTO dto = mapper.toDTO(purchase);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getSupplierId()).isEqualTo(10L);
        assertThat(dto.getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    void toEntity_shouldMapDTOToEntity() {
        UUID userId = UUID.randomUUID();

        PurchaseDTO dto = new PurchaseDTO();
        dto.setId(5L);
        dto.setSupplierId(20L);
        dto.setUserId(userId);
        dto.setStatus(PurchaseDTO.StatusShopping.REGISTERED);
        dto.setTotal(BigDecimal.valueOf(2000.00));

        Purchase purchase = mapper.toEntity(dto);

        assertThat(purchase).isNotNull();
        assertThat(purchase.getPurchaseId()).isEqualTo(5L);
        assertThat(purchase.getSupplier()).isNotNull();
        assertThat(purchase.getSupplier().getSupplierId()).isEqualTo(20L);
        assertThat(purchase.getUser()).isNotNull();
        assertThat(purchase.getUser().getUserId()).isEqualTo(userId);
    }

    @Test
    void mapStatus_shouldConvertStatusShoppingToStatusShopping() {
        Purchase.statusShopping draft = mapper.mapStatus(PurchaseDTO.StatusShopping.DRAFT);
        assertThat(draft).isEqualTo(Purchase.statusShopping.DRAFT);

        Purchase.statusShopping registered = mapper.mapStatus(PurchaseDTO.StatusShopping.REGISTERED);
        assertThat(registered).isEqualTo(Purchase.statusShopping.REGISTERED);

        Purchase.statusShopping annulled = mapper.mapStatus(PurchaseDTO.StatusShopping.ANNULLED);
        assertThat(annulled).isEqualTo(Purchase.statusShopping.ANNULLED);
    }

    @Test
    void mapStatus_shouldReturnNullWhenStatusIsNull() {
        assertThat(mapper.mapStatus(null)).isNull();
    }

    @Test
    void toDTO_shouldMapAllFields() {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(15L);

        User user = new User();
        user.setUserId(UUID.randomUUID());

        OffsetDateTime now = OffsetDateTime.now();

        Purchase purchase = new Purchase();
        purchase.setPurchaseId(100L);
        purchase.setSupplier(supplier);
        purchase.setUser(user);
        purchase.setDate(now);
        purchase.setStatus(Purchase.statusShopping.REGISTERED);
        purchase.setTotal(BigDecimal.valueOf(5000.00));
        purchase.setCreatedAt(now);
        purchase.setUpdatedAt(now);

        PurchaseDTO dto = mapper.toDTO(purchase);

        assertThat(dto.getId()).isEqualTo(100L);
        assertThat(dto.getSupplierId()).isEqualTo(15L);
        assertThat(dto.getUserId()).isEqualTo(user.getUserId());
        assertThat(dto.getDate()).isEqualTo(now);
        assertThat(dto.getTotal()).isEqualByComparingTo("5000.00");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toDTO_shouldHandleNullRelationships() {
        Purchase purchase = new Purchase();
        purchase.setPurchaseId(1L);
        purchase.setSupplier(null);
        purchase.setUser(null);

        PurchaseDTO dto = mapper.toDTO(purchase);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getSupplierId()).isNull();
        assertThat(dto.getUserId()).isNull();
    }

    @Test
    void toEntity_shouldHandleNullRelationships() {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setId(1L);
        dto.setSupplierId(null);
        dto.setUserId(null);

        Purchase purchase = mapper.toEntity(dto);

        assertThat(purchase.getPurchaseId()).isEqualTo(1L);
    }
}

