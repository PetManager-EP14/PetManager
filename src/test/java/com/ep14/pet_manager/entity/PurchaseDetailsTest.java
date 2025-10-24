package com.ep14.pet_manager.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

class PurchaseDetailsTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        PurchaseDetails details = new PurchaseDetails();
        OffsetDateTime now = OffsetDateTime.now();
        Purchase purchase = new Purchase();
        Product product = new Product();

        details.setPurchaseDetailId(1L);
        details.setPurchase(purchase);
        details.setProduct(product);
        details.setAmount(BigDecimal.valueOf(10.5));
        details.setCreatedAt(now);

        assertThat(details.getPurchaseDetailId()).isEqualTo(1L);
        assertThat(details.getPurchase()).isEqualTo(purchase);
        assertThat(details.getProduct()).isEqualTo(product);
        assertThat(details.getAmount()).isEqualByComparingTo("10.5");
        assertThat(details.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testJsonCreatorConstructor() {
        OffsetDateTime now = OffsetDateTime.now();
        Purchase purchase = new Purchase();
        Product product = new Product();

        PurchaseDetails details = new PurchaseDetails(
                5L,
                purchase,
                product,
                BigDecimal.valueOf(15.75),
                now
        );

        assertThat(details.getPurchaseDetailId()).isEqualTo(5L);
        assertThat(details.getPurchase()).isEqualTo(purchase);
        assertThat(details.getProduct()).isEqualTo(product);
        assertThat(details.getAmount()).isEqualByComparingTo("15.75");
        assertThat(details.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testSetterWithNullValues() {
        PurchaseDetails details = new PurchaseDetails();

        details.setPurchaseDetailId(null);
        details.setPurchase(null);
        details.setProduct(null);
        details.setAmount(null);
        details.setCreatedAt(null);

        assertThat(details.getPurchaseDetailId()).isNull();
        assertThat(details.getPurchase()).isNull();
        assertThat(details.getProduct()).isNull();
        assertThat(details.getAmount()).isNull();
        assertThat(details.getCreatedAt()).isNull();
    }

    @Test
    void testDifferentBigDecimalValues() {
        PurchaseDetails details = new PurchaseDetails();

        details.setAmount(BigDecimal.ZERO);
        assertThat(details.getAmount()).isEqualByComparingTo("0");

        details.setAmount(new BigDecimal("-50.25"));
        assertThat(details.getAmount()).isEqualByComparingTo("-50.25");

        details.setAmount(new BigDecimal("999999.999"));
        assertThat(details.getAmount()).isEqualByComparingTo("999999.999");
    }

    @Test
    void testDifferentDates() {
        PurchaseDetails details = new PurchaseDetails();
        OffsetDateTime past = OffsetDateTime.now().minusDays(10);
        OffsetDateTime future = OffsetDateTime.now().plusDays(10);

        details.setCreatedAt(past);
        assertThat(details.getCreatedAt()).isEqualTo(past);

        details.setCreatedAt(future);
        assertThat(details.getCreatedAt()).isEqualTo(future);
    }

    @Test
    void testRelationships() {
        PurchaseDetails details = new PurchaseDetails();
        Purchase purchase = new Purchase();
        Product product = new Product();

        details.setPurchase(purchase);
        details.setProduct(product);

        assertThat(details.getPurchase()).isEqualTo(purchase);
        assertThat(details.getProduct()).isEqualTo(product);
    }

    @Test
    void testMultipleDetailsWithSamePurchase() {
        Purchase purchase = new Purchase();
        Product product = new Product();

        PurchaseDetails details1 = new PurchaseDetails();
        details1.setPurchaseDetailId(1L);
        details1.setPurchase(purchase);
        details1.setProduct(product);

        PurchaseDetails details2 = new PurchaseDetails();
        details2.setPurchaseDetailId(2L);
        details2.setPurchase(purchase);
        details2.setProduct(product);

        assertThat(details1.getPurchase()).isEqualTo(purchase);
        assertThat(details2.getPurchase()).isEqualTo(purchase);
        assertThat(details1.getPurchase()).isEqualTo(details2.getPurchase());
    }

    @Test
    void testMultipleDetailsWithSameProduct() {
        Purchase purchase = new Purchase();
        Product product = new Product();

        PurchaseDetails details1 = new PurchaseDetails();
        details1.setPurchaseDetailId(1L);
        details1.setPurchase(purchase);
        details1.setProduct(product);

        PurchaseDetails details2 = new PurchaseDetails();
        details2.setPurchaseDetailId(2L);
        details2.setPurchase(purchase);
        details2.setProduct(product);

        assertThat(details1.getProduct()).isEqualTo(product);
        assertThat(details2.getProduct()).isEqualTo(product);
        assertThat(details1.getProduct()).isEqualTo(details2.getProduct());
    }
}

