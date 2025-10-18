package com.ep14.pet_manager.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.ep14.pet_manager.dto.SaleDTO;
import com.ep14.pet_manager.dto.SaleDetailDTO;
import com.ep14.pet_manager.entity.Product;
import com.ep14.pet_manager.entity.Sale;
import com.ep14.pet_manager.entity.SaleDetails;

@Mapper(componentModel = "spring")
public interface SaleMapper {


    // Sale -> SaleDTO
    @Mapping(source = "saleId", target = "saleId")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "saleDetails", target = "details")
    SaleDTO toDTO(Sale sale);

    // SaleDTO -> Sale
    @Mapping(source = "userId", target = "user.userId")
    @Mapping(source = "details", target = "saleDetails")
    @Mapping(target = "saleNotification", ignore = true)
    Sale toEntity(SaleDTO dto);

    // Element mapping: SaleDetailDTO -> SaleDetails
    @Mapping(source = "productId", target = "product")
    @Mapping(target = "sale", ignore = true)    
    @Mapping(target = "createdAt", ignore = true)
    SaleDetails toEntity(SaleDetailDTO dto);

    // Element mapping: SaleDetails -> SaleDetailDTO
    @Mapping(source = "product.productId", target = "productId")
    SaleDetailDTO toDTO(SaleDetails entity);

    // Helper to map productId <-> Product
    default Product map(Long productId) {
        if (productId == null) return null;
        Product p = new Product();
        p.setProductId(productId);
        return p;
    }
    default Long map(Product product) {
        return product == null ? null : product.getProductId();
    }

    @AfterMapping
    default void setBackReference(@MappingTarget Sale sale) {
        if (sale != null && sale.getSaleDetails() != null) {
            for (SaleDetails d : sale.getSaleDetails()) {
                d.setSale(sale);
            }
        }
    }
}
