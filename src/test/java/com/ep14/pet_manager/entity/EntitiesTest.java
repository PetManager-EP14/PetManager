package com.ep14.pet_manager.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

class EntitiesTest {

    @Test
    void testAccessLog() {
        AccessLog log = new AccessLog();
        UUID uid = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        log.setAccessLogId(1L);
        log.setUserId(uid);
        log.setPath("/api/test");
        log.setMethod("GET");
        log.setRequiredPermission("perm.read");
        log.setDecision("ALLOWED");
        log.setReason("OK");
        log.setRemoteAddr("127.0.0.1");
        log.setCreatedAt(now);
        log.setUpdatedAt(now);

        assertThat(log.getAccessLogId()).isEqualTo(1L);
        assertThat(log.getUserId()).isEqualTo(uid);
        assertThat(log.getDecision()).isEqualTo("ALLOWED");
        assertThat(log.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testPermissionAndRole() {
        Permission perm = new Permission();
        perm.setPermissionId(10L);
        perm.setCode("user.read");
        perm.setDescription("Read users");

        Role role = new Role();
        role.setRoleId(1L);
        role.setCode("ADMIN");
        role.setDescription("Administrator");
        role.setPermissions(Set.of(perm));

        perm.setRoles(Set.of(role));

        assertThat(role.getPermissions()).contains(perm);
        assertThat(perm.getRoles()).contains(role);
    }

    @Test
    void testRoleJsonCreatorConstructor() {
        OffsetDateTime now = OffsetDateTime.now();
        Role role = new Role(1L, "USER", "Standard", now, now, new ArrayList<>());
        assertThat(role.getCode()).isEqualTo("USER");
        assertThat(role.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testUserEntityAndRole() {
        Role role = new Role();
        role.setCode("CLIENT");
        UUID uid = UUID.randomUUID();

        User u = new User(uid, "Juan", "juan@test.com", "300", "Calle 1", "hash",
                OffsetDateTime.now(), OffsetDateTime.now(), role, List.of(), List.of());

        assertThat(u.getRole().getCode()).isEqualTo("CLIENT");
        assertThat(u.getEmail()).isEqualTo("juan@test.com");
        assertThat(u.getName()).isEqualTo("Juan");
    }

    @Test
    void testProductEntityAndJsonCreator() {
        OffsetDateTime now = OffsetDateTime.now();
        Product p = new Product(
                1L,
                "Producto A",
                "Alimentos",
                BigDecimal.TEN,
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(1500),
                now,
                now,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        assertThat(p.getProductId()).isEqualTo(1L);
        assertThat(p.getCategory()).isEqualTo("Alimentos");
        assertThat(p.getPriceSale()).isEqualTo(BigDecimal.valueOf(1500));
    }

    @Test
    void testSupplierAndJsonCreator() {
        OffsetDateTime now = OffsetDateTime.now();
        Supplier s = new Supplier(
                5L,
                "Proveedor A",
                "prov@test.com",
                "900111222",
                "12345",
                now,
                now,
                new ArrayList<>(),
                new ArrayList<>()
        );

        assertThat(s.getSupplierId()).isEqualTo(5L);
        assertThat(s.getEmail()).isEqualTo("prov@test.com");
        assertThat(s.getNit()).isEqualTo("900111222");
    }

    @Test
    void testPurchaseAndJsonCreator() {
        OffsetDateTime now = OffsetDateTime.now();
        Supplier s = new Supplier();
        User u = new User();

        Purchase p = new Purchase(
                10L,
                s,
                now,
                Purchase.statusShopping.REGISTERED,
                BigDecimal.valueOf(50000),
                now,
                now,
                u,
                new ArrayList<>()
        );

        assertThat(p.getStatus()).isEqualTo(Purchase.statusShopping.REGISTERED);
        assertThat(p.getTotal()).isEqualTo(BigDecimal.valueOf(50000));
        assertThat(p.getUser()).isEqualTo(u);
    }

    @Test
    void testPurchaseDetailsAndJsonCreator() {
        OffsetDateTime now = OffsetDateTime.now();
        Purchase purchase = new Purchase();
        Product product = new Product();

        PurchaseDetails pd = new PurchaseDetails(
                1L,
                purchase,
                product,
                BigDecimal.ONE,
                now
        );

        assertThat(pd.getAmount()).isEqualTo(BigDecimal.ONE);
        assertThat(pd.getCreatedAt()).isEqualTo(now);
        assertThat(pd.getProduct()).isNotNull();
    }

    @Test
    void testSaleAndJsonCreator() {
        OffsetDateTime now = OffsetDateTime.now();
        User u = new User();

        Sale sale = new Sale(
                1L,
                u,
                now,
                Sale.paymentMethod.CARD,
                Sale.saleStatus.REGISTERED,
                BigDecimal.valueOf(20000),
                now,
                now,
                null,
                new ArrayList<>()
        );

        assertThat(sale.getStatus()).isEqualTo(Sale.saleStatus.REGISTERED);
        assertThat(sale.getTotal()).isEqualTo(BigDecimal.valueOf(20000));
        assertThat(sale.getMethod()).isEqualTo(Sale.paymentMethod.CARD);
    }

    @Test
    void testSaleNotificationAndJsonCreator() {
        OffsetDateTime now = OffsetDateTime.now();
        Sale sale = new Sale();

        SaleNotification sn = new SaleNotification(
                1L,
                sale,
                now,
                SaleNotification.media.EMAIL,
                SaleNotification.type.HIGH_ROTATION,
                now
        );

        assertThat(sn.getMedia()).isEqualTo(SaleNotification.media.EMAIL);
        assertThat(sn.getType()).isEqualTo(SaleNotification.type.HIGH_ROTATION);
        assertThat(sn.getSale()).isNotNull();
    }

    @Test
    void testSupplierProductsAndJsonCreator() {
        OffsetDateTime now = OffsetDateTime.now();
        Product p = new Product();
        Supplier s = new Supplier();

        SupplierProducts sp = new SupplierProducts(
                p,
                s,
                BigDecimal.valueOf(100),
                5,
                true,
                now,
                now
        );

        assertThat(sp.getCostRef()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(sp.getSupplier()).isEqualTo(s);
        assertThat(sp.isActive()).isTrue();
    }

    @Test
    void testSupplierProductIdEqualsAndHash() {
        SupplierProductId id1 = new SupplierProductId();
        SupplierProductId id2 = new SupplierProductId();

        assertThat(id1.equals(id2)).isTrue();
        assertThat(id1).hasSameHashCodeAs(id2);
    }

    @Test
    void testUserPermissionAndEmbeddedId() {
        UUID uuid = UUID.randomUUID();
        UserPermission.Id id = new UserPermission.Id(uuid, 77L);
        UserPermission up = new UserPermission();
        up.setId(id);

        assertThat(up.getId().getUserId()).isEqualTo(uuid);
        assertThat(up.getId().getPermissionId()).isEqualTo(77L);
    }

    @Test
    void testEnumsCoverage() {
        assertThat(Sale.paymentMethod.valueOf("CARD")).isEqualTo(Sale.paymentMethod.CARD);
        assertThat(Sale.saleStatus.valueOf("REGISTERED")).isEqualTo(Sale.saleStatus.REGISTERED);
        assertThat(Purchase.statusShopping.valueOf("DRAFT")).isEqualTo(Purchase.statusShopping.DRAFT);
        assertThat(SaleNotification.media.valueOf("PUSH")).isEqualTo(SaleNotification.media.PUSH);
        assertThat(SaleNotification.type.valueOf("HIGH_VOLUMEN")).isEqualTo(SaleNotification.type.HIGH_VOLUMEN);
    }
}