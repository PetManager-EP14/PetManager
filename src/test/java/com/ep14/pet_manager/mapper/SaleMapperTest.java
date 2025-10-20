package com.ep14.pet_manager.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ep14.pet_manager.dto.SaleDTO;
import com.ep14.pet_manager.dto.SaleDetailDTO;
import com.ep14.pet_manager.entity.Product;
import com.ep14.pet_manager.entity.Sale;
import com.ep14.pet_manager.entity.SaleDetails;
import com.ep14.pet_manager.entity.User;

class SaleMapperTest {

    private SaleMapper mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(SaleMapper.class);
    }

    // --- 1️⃣ Test: Sale -> SaleDTO ---
    @Test
    void toDTO_shouldMapEntityToDTO() {
        User user = new User();
        user.setUserId(UUID.randomUUID());

        Product product = new Product();
        product.setProductId(101L);

        SaleDetails detail = new SaleDetails();
        detail.setSaleDetailId(1L);
        detail.setProduct(product);
        detail.setAmount(BigDecimal.valueOf(2));

        Sale sale = new Sale();
        sale.setSaleId(55L);
        sale.setUser(user);
        sale.setSaleDetails(List.of(detail));

        SaleDTO dto = mapper.toDTO(sale);

        assertThat(dto).isNotNull();
        assertThat(dto.getSaleId()).isEqualTo(55L);
        assertThat(dto.getUserId()).isEqualTo(user.getUserId());
        assertThat(dto.getDetails()).hasSize(1);
        assertThat(dto.getDetails().get(0).getProductId()).isEqualTo(101L);
        assertThat(dto.getDetails().get(0).getAmount()).isEqualTo(BigDecimal.valueOf(2));
    }

    // --- 2️⃣ Test: SaleDTO -> Sale ---
    @Test
    void toEntity_shouldMapDTOToEntityIgnoringDetailsAndNotification() {
        UUID userId = UUID.randomUUID();

        SaleDTO dto = new SaleDTO();
        dto.setSaleId(200L);
        dto.setUserId(userId);

        Sale sale = mapper.toEntity(dto);

        assertThat(sale).isNotNull();
        assertThat(sale.getSaleId()).isEqualTo(200L);
        assertThat(sale.getUser()).isNotNull();
        assertThat(sale.getUser().getUserId()).isEqualTo(userId);
        assertThat(sale.getSaleDetails()).isEmpty(); // Ignorado por @Mapping
        assertThat(sale.getSaleNotification()).isNull(); // Ignorado explícitamente
    }

    // --- 3️⃣ Test: SaleDetailDTO -> SaleDetails ---
    @Test
    void toEntity_shouldMapSaleDetailDTOToEntity() {
        SaleDetailDTO dto = new SaleDetailDTO();
        dto.setSaleDetailId(10L);
        dto.setProductId(999L);
        dto.setAmount(BigDecimal.valueOf(3));

        SaleDetails entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getSaleDetailId()).isEqualTo(10L);
        assertThat(entity.getProduct()).isNotNull();
        assertThat(entity.getProduct().getProductId()).isEqualTo(999L);
        assertThat(entity.getAmount()).isEqualTo(BigDecimal.valueOf(3));
        assertThat(entity.getSale()).isNull();
        assertThat(entity.getCreatedAt()).isNotNull();
    }

    // --- 4️⃣ Test: SaleDetails -> SaleDetailDTO ---
    @Test
    void toDTO_shouldMapSaleDetailsToDTO() {
        Product product = new Product();
        product.setProductId(888L);

        SaleDetails entity = new SaleDetails();
        entity.setSaleDetailId(20L);
        entity.setProduct(product);
        entity.setAmount(BigDecimal.valueOf(5));

        SaleDetailDTO dto = mapper.toDTO(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getSaleDetailId()).isEqualTo(20L);
        assertThat(dto.getProductId()).isEqualTo(888L);
        assertThat(dto.getAmount()).isEqualTo(BigDecimal.valueOf(5));
    }

    // --- 5️⃣ Test: Helper map(Long productId) ---
    @Test
    void map_shouldConvertProductIdToProduct() {
        Product product = mapper.map(77L);
        assertThat(product).isNotNull();
        assertThat(product.getProductId()).isEqualTo(77L);
    }

    @Test
    void map_shouldReturnNullWhenProductIdIsNull() {
        assertThat(mapper.map((Long) null)).isNull();
    }

    // --- 6️⃣ Test: Helper map(Product product) ---
    @Test
    void map_shouldConvertProductToProductId() {
        Product p = new Product();
        p.setProductId(123L);
        assertThat(mapper.map(p)).isEqualTo(123L);
    }

    @Test
    void map_shouldReturnNullWhenProductIsNull() {
        assertThat(mapper.map((Product) null)).isNull();
    }

    // --- 7️⃣ Test: @AfterMapping fillDetailInfo ---
    @Test
    void fillDetailInfo_shouldPopulateDetailsInDTO() {
        Product product = new Product();
        product.setProductId(55L);

        SaleDetails detail = new SaleDetails();
        detail.setSaleDetailId(99L);
        detail.setProduct(product);
        detail.setAmount(BigDecimal.ONE);

        Sale sale = new Sale();
        sale.setSaleDetails(List.of(detail));

        SaleDTO dto = new SaleDTO();

        mapper.fillDetailInfo(dto, sale);

        assertThat(dto.getDetails()).isNotNull().hasSize(1);
        assertThat(dto.getDetails().get(0).getSaleDetailId()).isEqualTo(99L);
        assertThat(dto.getDetails().get(0).getProductId()).isEqualTo(55L);
        assertThat(dto.getDetails().get(0).getAmount()).isEqualTo(BigDecimal.ONE);
    }

    @Test
    void fillDetailInfo_shouldHandleNullDetailsGracefully() {
        Sale sale = new Sale(); // saleDetails = null
        SaleDTO dto = new SaleDTO();
        mapper.fillDetailInfo(dto, sale);
        assertThat(dto.getDetails()).isNullOrEmpty();
    }
}
