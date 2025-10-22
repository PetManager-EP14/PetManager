package com.ep14.pet_manager.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

class PurchaseDTOTest {

    @Test
    void testBuilderCreatesValidObject() {
        OffsetDateTime now = OffsetDateTime.now();
        UUID userId = UUID.randomUUID();
        List<Long> detailIds = List.of(10L, 20L);

        PurchaseDTO dto = new PurchaseDTO.Builder()
                .id(1L)
                .supplierId(5L)
                .date(now)
                .status(PurchaseDTO.StatusShopping.REGISTERED)
                .total(new BigDecimal("1234.56"))
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .userId(userId)
                .shoppingDetailIds(detailIds)
                .build();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getSupplierId()).isEqualTo(5L);
        assertThat(dto.getDate()).isEqualTo(now);
        assertThat(dto.getStatus()).isEqualTo(PurchaseDTO.StatusShopping.REGISTERED);
        assertThat(dto.getTotal()).isEqualByComparingTo("1234.56");
        assertThat(dto.getCreatedAt()).isEqualTo(now.minusDays(1));
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
        assertThat(dto.getUserId()).isEqualTo(userId);
        assertThat(dto.getShoppingDetailIds()).containsExactly(10L, 20L);
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        OffsetDateTime created = OffsetDateTime.now();
        OffsetDateTime updated = created.plusDays(2);
        UUID userId = UUID.randomUUID();
        List<Long> ids = List.of(7L, 8L, 9L);

        PurchaseDTO dto = new PurchaseDTO();
        dto.setId(9L);
        dto.setSupplierId(11L);
        dto.setDate(created);
        dto.setStatus(PurchaseDTO.StatusShopping.DRAFT);
        dto.setTotal(new BigDecimal("500.00"));
        dto.setCreatedAt(created);
        dto.setUpdatedAt(updated);
        dto.setUserId(userId);
        dto.setShoppingDetailIds(ids);

        assertThat(dto.getId()).isEqualTo(9L);
        assertThat(dto.getSupplierId()).isEqualTo(11L);
        assertThat(dto.getDate()).isEqualTo(created);
        assertThat(dto.getStatus()).isEqualTo(PurchaseDTO.StatusShopping.DRAFT);
        assertThat(dto.getTotal()).isEqualByComparingTo("500.00");
        assertThat(dto.getCreatedAt()).isEqualTo(created);
        assertThat(dto.getUpdatedAt()).isEqualTo(updated);
        assertThat(dto.getUserId()).isEqualTo(userId);
        assertThat(dto.getShoppingDetailIds()).containsExactlyElementsOf(ids);
    }

    @Test
    void testEnumValues() {
        assertThat(PurchaseDTO.StatusShopping.valueOf("DRAFT")).isEqualTo(PurchaseDTO.StatusShopping.DRAFT);
        assertThat(PurchaseDTO.StatusShopping.values()).containsExactly(
                PurchaseDTO.StatusShopping.DRAFT,
                PurchaseDTO.StatusShopping.REGISTERED,
                PurchaseDTO.StatusShopping.ANNULLED
        );
    }
}