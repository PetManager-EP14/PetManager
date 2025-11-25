package com.ep14.pet_manager.mapper;

import com.ep14.pet_manager.dto.PurchaseDTO;
import com.ep14.pet_manager.dto.PurchaseDetailDTO;
import com.ep14.pet_manager.entity.Product;
import com.ep14.pet_manager.entity.Purchase;
import com.ep14.pet_manager.entity.PurchaseDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal; 

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);

    @Mappings({
        @Mapping(source = "purchaseId", target = "id"),
        @Mapping(source = "supplier.supplierId", target = "supplierId"),
        @Mapping(source = "user.userId", target = "userId"),
        @Mapping(source = "createdAt", target = "createdAt"),
        @Mapping(source = "updatedAt", target = "updatedAt"),
        @Mapping(source = "purchaseDetails", target = "details") 
    })
    PurchaseDTO toDTO(Purchase purchase);
    @Mappings({
        @Mapping(source = "id", target = "purchaseId"),
        @Mapping(source = "supplierId", target = "supplier.supplierId"),
        @Mapping(source = "userId", target = "user.userId"),
        @Mapping(source = "createdAt", target = "createdAt"),
        @Mapping(source = "updatedAt", target = "updatedAt"),

        @Mapping(target = "purchaseDetails", ignore = true) 
    })
    Purchase toEntity(PurchaseDTO purchaseDTO);
    @Mappings({
        @Mapping(source = "product.productId", target = "productId"),
        @Mapping(source = "amount", target = "amount"),
        @Mapping(source = "purchaseDetailId", target = "purchaseDetailId"),
        @Mapping(source = "product.name", target = "productName"), 
        @Mapping(source = "product.priceShopping", target = "unitPrice") 
    })
    PurchaseDetailDTO toDTO(PurchaseDetails entity);

    @Mappings({
        // Utiliza el método 'map(Long productId)' para crear una entidad Product stub 
        @Mapping(source = "productId", target = "product"),
        @Mapping(target = "purchase", ignore = true), // Ignorar la relación Purchase cíclica
        @Mapping(target = "createdAt", ignore = true)
    })
    PurchaseDetails toEntity(PurchaseDetailDTO dto);


    // Helper para mapear Long productId <-> Product entity 
    default Product map(Long productId) {
        if (productId == null) return null;
        Product p = new Product();
        p.setProductId(productId);
        return p;
    }
    Purchase.statusShopping mapStatus(PurchaseDTO.StatusShopping status);

}
