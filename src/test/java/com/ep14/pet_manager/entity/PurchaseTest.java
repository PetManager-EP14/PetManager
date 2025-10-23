package com.ep14.pet_manager.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

class PurchaseTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Purchase purchase = new Purchase();
        OffsetDateTime now = OffsetDateTime.now();
        Supplier supplier = new Supplier();
        User user = new User();
        List<PurchaseDetails> details = new ArrayList<>();

        purchase.setPurchaseId(1L);
        purchase.setSupplier(supplier);
        purchase.setDate(now);
        purchase.setStatus(Purchase.statusShopping.DRAFT);
        purchase.setTotal(BigDecimal.valueOf(1000.50));
        purchase.setCreatedAt(now);
        purchase.setUpdatedAt(now);
        purchase.setUser(user);
        purchase.setPurchaseDetails(details);

        assertThat(purchase.getPurchaseId()).isEqualTo(1L);
        assertThat(purchase.getSupplier()).isEqualTo(supplier);
        assertThat(purchase.getDate()).isEqualTo(now);
        assertThat(purchase.getStatus()).isEqualTo(Purchase.statusShopping.DRAFT);
        assertThat(purchase.getTotal()).isEqualByComparingTo("1000.50");
        assertThat(purchase.getCreatedAt()).isEqualTo(now);
        assertThat(purchase.getUpdatedAt()).isEqualTo(now);
        assertThat(purchase.getUser()).isEqualTo(user);
        assertThat(purchase.getPurchaseDetails()).isEqualTo(details);
    }

    @Test
    void testJsonCreatorConstructor() {
        OffsetDateTime now = OffsetDateTime.now();
        Supplier supplier = new Supplier();
        User user = new User();
        List<PurchaseDetails> details = new ArrayList<>();

        Purchase purchase = new Purchase(
                10L,
                supplier,
                now,
                Purchase.statusShopping.REGISTERED,
                BigDecimal.valueOf(50000),
                now,
                now,
                user,
                details
        );

        assertThat(purchase.getPurchaseId()).isEqualTo(10L);
        assertThat(purchase.getSupplier()).isEqualTo(supplier);
        assertThat(purchase.getDate()).isEqualTo(now);
        assertThat(purchase.getStatus()).isEqualTo(Purchase.statusShopping.REGISTERED);
        assertThat(purchase.getTotal()).isEqualByComparingTo("50000");
        assertThat(purchase.getCreatedAt()).isEqualTo(now);
        assertThat(purchase.getUpdatedAt()).isEqualTo(now);
        assertThat(purchase.getUser()).isEqualTo(user);
        assertThat(purchase.getPurchaseDetails()).isEqualTo(details);
    }

    @Test
    void testEnumValues() {
        assertThat(Purchase.statusShopping.valueOf("DRAFT")).isEqualTo(Purchase.statusShopping.DRAFT);
        assertThat(Purchase.statusShopping.valueOf("REGISTERED")).isEqualTo(Purchase.statusShopping.REGISTERED);
        assertThat(Purchase.statusShopping.valueOf("ANNULLED")).isEqualTo(Purchase.statusShopping.ANNULLED);
        
        assertThat(Purchase.statusShopping.values()).containsExactly(
                Purchase.statusShopping.DRAFT,
                Purchase.statusShopping.REGISTERED,
                Purchase.statusShopping.ANNULLED
        );
    }

    @Test
    void testAllStatusValues() {
        Purchase purchase = new Purchase();
        
        purchase.setStatus(Purchase.statusShopping.DRAFT);
        assertThat(purchase.getStatus()).isEqualTo(Purchase.statusShopping.DRAFT);
        
        purchase.setStatus(Purchase.statusShopping.REGISTERED);
        assertThat(purchase.getStatus()).isEqualTo(Purchase.statusShopping.REGISTERED);
        
        purchase.setStatus(Purchase.statusShopping.ANNULLED);
        assertThat(purchase.getStatus()).isEqualTo(Purchase.statusShopping.ANNULLED);
    }

    @Test
    void testSetterWithNullValues() {
        Purchase purchase = new Purchase();
        
        purchase.setPurchaseId(null);
        purchase.setSupplier(null);
        purchase.setDate(null);
        purchase.setStatus(null);
        purchase.setTotal(null);
        purchase.setUser(null);
        purchase.setPurchaseDetails(null);

        assertThat(purchase.getPurchaseId()).isNull();
        assertThat(purchase.getSupplier()).isNull();
        assertThat(purchase.getDate()).isNull();
        assertThat(purchase.getStatus()).isNull();
        assertThat(purchase.getTotal()).isNull();
        assertThat(purchase.getUser()).isNull();
        assertThat(purchase.getPurchaseDetails()).isNull();
    }

    @Test
    void testDifferentBigDecimalValues() {
        Purchase purchase = new Purchase();
        
        purchase.setTotal(BigDecimal.ZERO);
        assertThat(purchase.getTotal()).isEqualByComparingTo("0");
        
        purchase.setTotal(new BigDecimal("-100.50"));
        assertThat(purchase.getTotal()).isEqualByComparingTo("-100.50");
        
        purchase.setTotal(new BigDecimal("999999.99"));
        assertThat(purchase.getTotal()).isEqualByComparingTo("999999.99");
    }

    @Test
    void testDifferentDates() {
        Purchase purchase = new Purchase();
        OffsetDateTime past = OffsetDateTime.now().minusDays(10);
        OffsetDateTime future = OffsetDateTime.now().plusDays(10);
        
        purchase.setDate(past);
        purchase.setCreatedAt(past);
        purchase.setUpdatedAt(future);
        
        assertThat(purchase.getDate()).isEqualTo(past);
        assertThat(purchase.getCreatedAt()).isEqualTo(past);
        assertThat(purchase.getUpdatedAt()).isEqualTo(future);
    }

    @Test
    void testPurchaseDetailsList() {
        Purchase purchase = new Purchase();
        List<PurchaseDetails> details = new ArrayList<>();
        
        PurchaseDetails detail1 = new PurchaseDetails();
        PurchaseDetails detail2 = new PurchaseDetails();
        details.add(detail1);
        details.add(detail2);
        
        purchase.setPurchaseDetails(details);
        
        assertThat(purchase.getPurchaseDetails()).hasSize(2);
        assertThat(purchase.getPurchaseDetails()).containsExactly(detail1, detail2);
    }

    @Test
    void testPurchaseDetailsEmptyList() {
        Purchase purchase = new Purchase();
        List<PurchaseDetails> emptyDetails = new ArrayList<>();
        
        purchase.setPurchaseDetails(emptyDetails);
        
        assertThat(purchase.getPurchaseDetails()).isNotNull();
        assertThat(purchase.getPurchaseDetails()).isEmpty();
    }

    @Test
    void testRelationships() {
        Purchase purchase = new Purchase();
        Supplier supplier = new Supplier();
        User user = new User();
        List<PurchaseDetails> details = new ArrayList<>();
        
        purchase.setSupplier(supplier);
        purchase.setUser(user);
        purchase.setPurchaseDetails(details);
        
        assertThat(purchase.getSupplier()).isEqualTo(supplier);
        assertThat(purchase.getUser()).isEqualTo(user);
        assertThat(purchase.getPurchaseDetails()).isEqualTo(details);
    }

    @Test
    void testMultiplePurchasesWithSameSupplier() {
        Supplier supplier = new Supplier();
        User user = new User();
        
        Purchase purchase1 = new Purchase();
        purchase1.setPurchaseId(1L);
        purchase1.setSupplier(supplier);
        purchase1.setUser(user);
        
        Purchase purchase2 = new Purchase();
        purchase2.setPurchaseId(2L);
        purchase2.setSupplier(supplier);
        purchase2.setUser(user);
        
        assertThat(purchase1.getSupplier()).isEqualTo(supplier);
        assertThat(purchase2.getSupplier()).isEqualTo(supplier);
        assertThat(purchase1.getSupplier()).isEqualTo(purchase2.getSupplier());
    }

    @Test
    void testMultiplePurchasesWithSameUser() {
        Supplier supplier = new Supplier();
        User user = new User();
        
        Purchase purchase1 = new Purchase();
        purchase1.setPurchaseId(1L);
        purchase1.setSupplier(supplier);
        purchase1.setUser(user);
        
        Purchase purchase2 = new Purchase();
        purchase2.setPurchaseId(2L);
        purchase2.setSupplier(supplier);
        purchase2.setUser(user);
        
        assertThat(purchase1.getUser()).isEqualTo(user);
        assertThat(purchase2.getUser()).isEqualTo(user);
        assertThat(purchase1.getUser()).isEqualTo(purchase2.getUser());
    }

    @Test
    void testCompletePurchaseFlow() {
        OffsetDateTime now = OffsetDateTime.now();
        Supplier supplier = new Supplier();
        supplier.setSupplierId(100L);
        
        User user = new User();
        
        Purchase purchase = new Purchase();
        purchase.setPurchaseId(1L);
        purchase.setSupplier(supplier);
        purchase.setDate(now);
        purchase.setStatus(Purchase.statusShopping.DRAFT);
        purchase.setTotal(BigDecimal.valueOf(15000.00));
        purchase.setCreatedAt(now);
        purchase.setUpdatedAt(now);
        purchase.setUser(user);

        purchase.setStatus(Purchase.statusShopping.REGISTERED);
        OffsetDateTime updated = OffsetDateTime.now();
        purchase.setUpdatedAt(updated);
        
        assertThat(purchase.getStatus()).isEqualTo(Purchase.statusShopping.REGISTERED);
        assertThat(purchase.getUpdatedAt()).isEqualTo(updated);
        assertThat(purchase.getTotal()).isEqualByComparingTo("15000.00");
    }
}

