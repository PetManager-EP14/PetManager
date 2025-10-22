package com.ep14.pet_manager.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EntitiesTest {

    // 1. AccessLog
    @Test
    void accessLog_shouldStoreAndRetrieveFields() {
        AccessLog log = new AccessLog();
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        log.setAccessLogId(1L);
        log.setUserId(id);
        log.setPath("/api/test");
        log.setMethod("GET");
        log.setDecision("DENIED");
        log.setRequiredPermission("user.read");
        log.setReason("no role");
        log.setRemoteAddr("127.0.0.1");
        log.setCreatedAt(now);
        log.setUpdatedAt(now);

        assertThat(log.getAccessLogId()).isEqualTo(1L);
        assertThat(log.getUserId()).isEqualTo(id);
        assertThat(log.getPath()).isEqualTo("/api/test");
        assertThat(log.getDecision()).isEqualTo("DENIED");
    }

    // 2. Permission
    @Test
    void permission_shouldHoldCodeAndDescription() {
        Permission p = new Permission();
        p.setPermissionId(5L);
        p.setCode("READ_USER");
        p.setDescription("Can read users");

        assertThat(p.getCode()).isEqualTo("READ_USER");
        assertThat(p.getDescription()).contains("read users");
    }

    // 3. Role
    @Test
    void role_shouldStoreCodeAndPermissions() {
        Role r = new Role();
        r.setRoleId(10L);
        r.setCode("ADMIN");
        r.setDescription("System administrator");

        Permission perm = new Permission();
        perm.setCode("ALL");
        r.setPermissions(Set.of(perm));

        assertThat(r.getCode()).isEqualTo("ADMIN");
        assertThat(r.getPermissions()).hasSize(1);
    }

    // 4. User
    @Test
    void user_shouldStoreBasicFieldsAndRole() {
        Role role = new Role();
        role.setCode("EMPLOYEE");

        User u = new User();
        UUID uid = UUID.randomUUID();
        u.setUserId(uid);
        u.setName("Brayan");
        u.setEmail("brayan@example.com");
        u.setPhone("3001112233");
        u.setAddress("Medellín");
        u.setPasswordHash("hash123");
        u.setRole(role);

        assertThat(u.getRole().getCode()).isEqualTo("EMPLOYEE");
        assertThat(u.getName()).isEqualTo("Brayan");
        assertThat(u.getEmail()).contains("@");
    }

    // 5. Product
    @Test
    void product_shouldHandlePricesAndRelations() {
        Product p = new Product();
        p.setName("Dog Food");
        p.setCategory("Pets");
        p.setStock(BigDecimal.TEN);
        p.setPriceShopping(BigDecimal.valueOf(5000));
        p.setPriceSale(BigDecimal.valueOf(8000));

        assertThat(p.getPriceSale()).isEqualByComparingTo("8000");
        assertThat(p.getCategory()).isEqualTo("Pets");
    }

    // 6. Supplier
    @Test
    void supplier_shouldStoreContactInfo() {
        Supplier s = new Supplier();
        s.setSupplierId(99L);
        s.setName("Acme");
        s.setEmail("acme@mail.com");
        s.setNit("900123456");
        s.setPhone("601234567");

        assertThat(s.getName()).isEqualTo("Acme");
        assertThat(s.getEmail()).endsWith("@mail.com");
    }

    // 7. SupplierProducts
    @Test
    void supplierProducts_shouldHandleFields() {
        Product prod = new Product();
        Supplier supp = new Supplier();

        SupplierProducts sp = new SupplierProducts();
        sp.setProduct(prod);
        sp.setSupplier(supp);
        sp.setCostRef(BigDecimal.valueOf(1000));
        sp.setLeadTimeDays(7);
        sp.setActive(true);

        assertThat(sp.getCostRef()).isEqualByComparingTo("1000");
        assertThat(sp.isActive()).isTrue();
        assertThat(sp.getLeadTimeDays()).isEqualTo(7);
    }

    // 8. SupplierProductId
    @Test
    void supplierProductId_shouldCompareEqualityCorrectly() {
        SupplierProductId id1 = new SupplierProductId();
        SupplierProductId id2 = new SupplierProductId();

        assertThat(id1.equals(id2)).isTrue();
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    // 9. Purchase
    @Test
    void purchase_shouldRelateUserAndSupplier() {
        Supplier s = new Supplier();
        s.setName("Proveedor 1");
        User u = new User();
        u.setName("Usuario 1");

        Purchase p = new Purchase();
        p.setSupplier(s);
        p.setUser(u);
        p.setTotal(BigDecimal.valueOf(12345));
        p.setStatus(Purchase.statusShopping.REGISTERED);

        assertThat(p.getSupplier().getName()).isEqualTo("Proveedor 1");
        assertThat(p.getUser().getName()).isEqualTo("Usuario 1");
        assertThat(p.getStatus()).isEqualTo(Purchase.statusShopping.REGISTERED);
    }

    // 10. PurchaseDetails
    @Test
    void purchaseDetails_shouldLinkProductAndPurchase() {
        PurchaseDetails d = new PurchaseDetails();
        d.setAmount(BigDecimal.valueOf(3));
        Product prod = new Product();
        Purchase purchase = new Purchase();
        d.setProduct(prod);
        d.setPurchase(purchase);

        assertThat(d.getAmount()).isEqualByComparingTo("3");
        assertThat(d.getProduct()).isNotNull();
        assertThat(d.getPurchase()).isNotNull();
    }

    // 11. Sale
    @Test
    void sale_shouldHandleStatusAndTotals() {
        Sale sale = new Sale();
        sale.setStatus(Sale.saleStatus.REGISTERED);
        sale.setMethod(Sale.paymentMethod.CARD);
        sale.setTotal(BigDecimal.valueOf(25000));

        assertThat(sale.getStatus()).isEqualTo(Sale.saleStatus.REGISTERED);
        assertThat(sale.getTotal()).isEqualByComparingTo("25000");
    }

    // 12. SaleDetails
    @Test
    void saleDetails_shouldLinkSaleAndProduct() {
        SaleDetails det = new SaleDetails();
        det.setAmount(BigDecimal.valueOf(5));
        Sale sale = new Sale();
        Product prod = new Product();
        det.setSale(sale);
        det.setProduct(prod);

        assertThat(det.getSale()).isNotNull();
        assertThat(det.getAmount()).isEqualByComparingTo("5");
    }

    // 13. SaleNotification
    @Test
    void saleNotification_shouldStoreEnumsAndDate() {
        SaleNotification n = new SaleNotification();
        n.setMedia(SaleNotification.media.EMAIL);
        n.setType(SaleNotification.type.HIGH_ROTATION);
        n.setShippingDate(OffsetDateTime.now());

        assertThat(n.getMedia()).isEqualTo(SaleNotification.media.EMAIL);
        assertThat(n.getType()).isEqualTo(SaleNotification.type.HIGH_ROTATION);
    }

    // 14. UserPermission + EmbeddedId
    @Test
    void userPermission_shouldLinkUserAndPermission() {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        Permission perm = new Permission();
        perm.setPermissionId(44L);

        UserPermission up = new UserPermission(user, perm);
        up.setId(new UserPermission.Id(user.getUserId(), perm.getPermissionId()));

        assertThat(up.getPermission().getPermissionId()).isEqualTo(44L);
        assertThat(up.getId().getUserId()).isEqualTo(user.getUserId());
        assertThat(up.getId().getPermissionId()).isEqualTo(44L);
    }
}
