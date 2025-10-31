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

    @Test
    void testBuilderWithMinimalFields() {
        PurchaseDTO dto = new PurchaseDTO.Builder()
                .id(1L)
                .build();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getSupplierId()).isNull();
        assertThat(dto.getDate()).isNull();
        assertThat(dto.getStatus()).isNull();
        assertThat(dto.getTotal()).isNull();
    }

    @Test
    void testBuilderWithAllStatusValues() {
        PurchaseDTO draft = new PurchaseDTO.Builder()
                .id(1L)
                .status(PurchaseDTO.StatusShopping.DRAFT)
                .build();
        assertThat(draft.getStatus()).isEqualTo(PurchaseDTO.StatusShopping.DRAFT);

        PurchaseDTO registered = new PurchaseDTO.Builder()
                .id(2L)
                .status(PurchaseDTO.StatusShopping.REGISTERED)
                .build();
        assertThat(registered.getStatus()).isEqualTo(PurchaseDTO.StatusShopping.REGISTERED);

        PurchaseDTO annulled = new PurchaseDTO.Builder()
                .id(3L)
                .status(PurchaseDTO.StatusShopping.ANNULLED)
                .build();
        assertThat(annulled.getStatus()).isEqualTo(PurchaseDTO.StatusShopping.ANNULLED);
    }

    @Test
    void testSettersWithNullValues() {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setId(null);
        dto.setSupplierId(null);
        dto.setDate(null);
        dto.setStatus(null);
        dto.setTotal(null);
        dto.setUserId(null);
        dto.setShoppingDetailIds(null);

        assertThat(dto.getId()).isNull();
        assertThat(dto.getSupplierId()).isNull();
        assertThat(dto.getDate()).isNull();
        assertThat(dto.getStatus()).isNull();
        assertThat(dto.getTotal()).isNull();
        assertThat(dto.getUserId()).isNull();
        assertThat(dto.getShoppingDetailIds()).isNull();
    }

    @Test
    void testBuilderWithEmptyList() {
        PurchaseDTO dto = new PurchaseDTO.Builder()
                .id(1L)
                .shoppingDetailIds(List.of())
                .build();

        assertThat(dto.getShoppingDetailIds()).isNotNull();
        assertThat(dto.getShoppingDetailIds()).isEmpty();
    }

    @Test
    void testBuilderWithMultipleDetailIds() {
        List<Long> detailIds = List.of(1L, 2L, 3L, 4L, 5L);
        PurchaseDTO dto = new PurchaseDTO.Builder()
                .id(1L)
                .shoppingDetailIds(detailIds)
                .build();

        assertThat(dto.getShoppingDetailIds()).hasSize(5);
        assertThat(dto.getShoppingDetailIds()).containsExactly(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    void testBuilderWithDifferentBigDecimalValues() {
        PurchaseDTO zero = new PurchaseDTO.Builder()
                .id(1L)
                .total(BigDecimal.ZERO)
                .build();
        assertThat(zero.getTotal()).isEqualByComparingTo("0");

        PurchaseDTO negative = new PurchaseDTO.Builder()
                .id(2L)
                .total(new BigDecimal("-100.50"))
                .build();
        assertThat(negative.getTotal()).isEqualByComparingTo("-100.50");

        PurchaseDTO large = new PurchaseDTO.Builder()
                .id(3L)
                .total(new BigDecimal("999999.99"))
                .build();
        assertThat(large.getTotal()).isEqualByComparingTo("999999.99");
    }

    @Test
    void testBuilderWithDifferentDates() {
        OffsetDateTime past = OffsetDateTime.now().minusDays(10);
        OffsetDateTime future = OffsetDateTime.now().plusDays(10);

        PurchaseDTO dto = new PurchaseDTO.Builder()
                .id(1L)
                .date(past)
                .createdAt(past)
                .updatedAt(future)
                .build();

        assertThat(dto.getDate()).isEqualTo(past);
        assertThat(dto.getCreatedAt()).isEqualTo(past);
        assertThat(dto.getUpdatedAt()).isEqualTo(future);
    }

    @Test
    void testBuilderWithSameUserId() {
        UUID userId = UUID.randomUUID();
        PurchaseDTO dto1 = new PurchaseDTO.Builder()
                .id(1L)
                .userId(userId)
                .build();
        PurchaseDTO dto2 = new PurchaseDTO.Builder()
                .id(2L)
                .userId(userId)
                .build();

        assertThat(dto1.getUserId()).isEqualTo(userId);
        assertThat(dto2.getUserId()).isEqualTo(userId);
        assertThat(dto1.getUserId()).isEqualTo(dto2.getUserId());
    }

    @Test
    void testBuilderIsImmutableAfterBuild() {
        PurchaseDTO.Builder builder = new PurchaseDTO.Builder()
                .id(1L)
                .supplierId(10L);

        PurchaseDTO dto1 = builder.build();
        PurchaseDTO dto2 = builder.id(2L).supplierId(20L).build();

        assertThat(dto1.getId()).isEqualTo(1L);
        assertThat(dto1.getSupplierId()).isEqualTo(10L);
        assertThat(dto2.getId()).isEqualTo(2L);
        assertThat(dto2.getSupplierId()).isEqualTo(20L);
    }
}